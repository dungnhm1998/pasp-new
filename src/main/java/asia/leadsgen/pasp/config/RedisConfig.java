package asia.leadsgen.pasp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;


	@Bean("jedisConnectionFactoryDatabase0")
	public JedisConnectionFactory getJedisConnectionFactoryDatabase0() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPort(port);
		factory.setDatabase(0);
		return factory;

	}

	@Bean("jedisConnectionFactory")
	public JedisConnectionFactory getJedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPort(port);
		factory.setDatabase(5);
		return factory;
	}

	@Bean("stringRedisSerializer")
	public StringRedisSerializer getStringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean("jdkSerializationRedisSerializer")
	public JdkSerializationRedisSerializer getJdkSeirializationRedisSerializer() {
		return new JdkSerializationRedisSerializer();
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, Object> getRedisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(getJedisConnectionFactory());
		template.setKeySerializer(getStringRedisSerializer());
		template.setValueSerializer(getJdkSeirializationRedisSerializer());
		return template;
	}

	@Bean("redisTemplateDatabase0")
	public RedisTemplate<String, Object> getRedisTemplateDatabase0() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(getJedisConnectionFactoryDatabase0());
		template.setKeySerializer(getStringRedisSerializer());
		template.setValueSerializer(getJdkSeirializationRedisSerializer());
		return template;
	}
}
