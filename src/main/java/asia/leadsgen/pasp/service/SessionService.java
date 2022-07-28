package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.model.dto.affiliate.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SessionService {

	@Autowired
	RedisService redisService;

	public UserInfo find(String id) {
		UserInfo session = (UserInfo) redisService.find(id);
		return session;
	}

}
