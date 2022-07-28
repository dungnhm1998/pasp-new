package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

	@Autowired
	@Qualifier(value = "redisTemplate")
	RedisTemplate redisTemplate;


	public Object find(String key) {
		return redisTemplate.opsForValue().get(key);
	}
}
