package asia.leadsgen.pasp.config;

import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.exception.AuthenticationException;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.JSONUtil;
import asia.leadsgen.pasp.util.ParamUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Component
public class UserPrivilegesFilter implements Filter {

	private static String AUTHORIZE_HEADER = "X-Authorization";
	private static int endOfBearer = 7;

	@Value("${psp.jwt.encryption.key}")
	private String encryptionKey;

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			String jwtToken = httpRequest.getHeader(AUTHORIZE_HEADER);
			if (StringUtils.isNotEmpty(jwtToken)) {

				String token = jwtToken.substring(endOfBearer, jwtToken.length());
				Map<String, String> userInfo = decodeJWT(token);

				String userId = userInfo.get(AppParams.USER_ID).toString();
				String aspRefId = userInfo.get(AppParams.ASP_REF_ID).toString();
				String owner = userInfo.get(AppParams.OWNER);

				Boolean isOwner = owner.equalsIgnoreCase("yes");
				String timeZone = userInfo.get(AppParams.TIMEZONE).toString();

				Set<String> domainNames = new HashSet<>();
				Set<String> module = new HashSet<>();
				Set<String> global = new HashSet<>();
				List<String> dropshipStoreIds = new ArrayList<>();

				if (!isOwner) {
					String domains = ParamUtil.getString(userInfo, AppParams.DOMAINS);
					String modulePermissions = ParamUtil.getString(userInfo, AppParams.MODULE_PERMISSIONS);
					String globalPermissions = ParamUtil.getString(userInfo, AppParams.GLOBAL_PERMISSIONS);
					domainNames = StringUtils.isEmpty(domains) ? null
							:new HashSet<>(Arrays.asList(domains.split(",")));
					module = StringUtils.isEmpty(modulePermissions) ? null
							:new HashSet<>(Arrays.asList(modulePermissions.split(",")));
					global = StringUtils.isEmpty(globalPermissions) ? null
							:new HashSet<>(Arrays.asList(globalPermissions.split(",")));

					dropshipStoreIds = CollectionUtils.isEmpty(domainNames) ? null
							:domainNames.stream().filter(d -> !d.contains(".")).collect(Collectors.toList());
				}

				BurgerContext burgerContext = new BurgerContext();
				burgerContext.setUserId(userId);
				burgerContext.setAspRefId(aspRefId);
				burgerContext.setIsOwner(isOwner);
				burgerContext.setDomainNames(domainNames);
				burgerContext.setTimeZone(timeZone.replaceAll("UTC", "GMT"));
				burgerContext.setModulePermissions(module);
				burgerContext.setGlobalPermissions(global);
				burgerContext.setDropshipStoreIds(dropshipStoreIds);
				log.info("BURGER CONTEXT --" + burgerContext.toString());
				httpRequest.setAttribute(AppConstants.BG_CONTEXT, burgerContext);
			}

			chain.doFilter(httpRequest, httpResponse);

		} catch (Exception e) {
			log.warn("UserPrivilegesFilter has Exception =" + e.getMessage());
			JSONObject json = new JSONObject();
			httpResponse.setHeader("Content-Type", "application/json");
			json.put("status", HttpStatus.UNAUTHORIZED.value());
			json.put("message", HttpStatus.UNAUTHORIZED.getReasonPhrase());
			httpResponse.getWriter().write(json.toString());

		}

	}

	public Map decodeJWT(String jwtToken) {
		try {
			String[] split_string = jwtToken.split("\\.");
			String base64EncodedBody = split_string[1];

			Base64 base64Url = new Base64();

			String body = new String((byte[]) base64Url.decode(base64EncodedBody));

			Map payload = JSONUtil.convertJSONToMap(body);

			return encryptMap(payload);
		} catch (Exception e) {
			throw new AuthenticationException(SystemError.INVALID_TOKEN);
		}
	}

	private String decryptS5(String input) throws Exception {

		byte[] cipherText = java.util.Base64.getDecoder().decode(input);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "SunJCE");

		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");

		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));

		return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
	}

	private Map encryptMap(Map orgMap) throws Exception {
		Map<String, String> resultMap = new HashMap<>();

		Set<String> keySet = orgMap.keySet();
		for (String key : keySet) {
			String newKey = decryptS5(key);
			String value = decryptS5(orgMap.get(key).toString());
			resultMap.put(newKey, value);
		}
		return resultMap;
	}

}
