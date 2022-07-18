package asia.leadsgen.pasp.util;

import asia.leadsgen.pasp.model.dto.payment.external.BankOfUSA.BankUSAChargeRequest;
import asia.leadsgen.pasp.model.dto.payment.external.BankOfUSA.BankUSAChargeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Log4j2
public class GlobalGatewayClient {

	@Value("${global.gateway.client.uri}")
	private static String uri;
	@Value("${global.gateway.client.gatewayId}")
	private static String gatewayId;
	@Value("${global.gateway.client.gatewayPassword}")
	private static String gatewayPassword;
	@Value("${global.gateway.client.hmacKey}")
	private static String hmacKey;
	@Value("${global.gateway.client.host}")
	private static String host;

	public static BankUSAChargeResponse purchase(BankUSAChargeRequest bankUSAChargeRequest) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		BankUSAChargeResponse responseBody = new BankUSAChargeResponse();
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
		String payeezyGatewayDate = now.format(formatter);
		String method = "POST";

		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, bankUSAChargeRequest.toJson()); // new
		String contentDigest = DigestUtils.sha1Hex(bankUSAChargeRequest.toJson());
		String contentType = "application/json";
		String accept = "application/json";
		SecretKeySpec keySpec = new SecretKeySpec(hmacKey.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);
		String hmac = Base64.getEncoder().encodeToString(mac.doFinal((method + "\n" + contentType + "\n" + contentDigest + "\n" + payeezyGatewayDate + "\n" + uri).getBytes()));

		Request request = new Request.Builder()
				.url(host + uri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.X_GGE4_DATE, payeezyGatewayDate)
				.addHeader(AppConstants.AUTHORIZATION, "GGE4_API " + gatewayId + ":" + hmac)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[BANK_OF_USA PURCHASE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[BANK_OF_USA PURCHASE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), BankUSAChargeResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());
		return responseBody;

	}

}
