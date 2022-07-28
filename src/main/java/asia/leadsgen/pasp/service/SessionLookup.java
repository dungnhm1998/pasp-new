package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.dto.affiliate.UserInfo;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.logging.Logger;

public class SessionLookup {

	private String url;

	@Autowired
	SessionService sessionService;
	@Autowired
	RedisService redisService;

	public  UserInfo get(String sessionId) {
		UserInfo user = new UserInfo();
		UserInfo session = sessionService.find(sessionId);
		if (!ObjectUtils.isEmpty(session)) {
			return session;
		}

		return user;
	}

	public static UserInfo getUserInfoFromContext(BurgerContext context) {
		UserInfo user = new UserInfo();
		user.setAffId(context.getUserId());
		user.setId(context.getAspRefId());
		user.setEmail(context.getEmail());
		return user;
	}

	private static final Logger LOGGER = Logger.getLogger(SessionLookup.class.getName());

}
