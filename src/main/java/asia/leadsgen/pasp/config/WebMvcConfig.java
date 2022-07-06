package asia.leadsgen.pasp.config;

import asia.leadsgen.pasp.middleware.HTTPMonitoringInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
//@EnableSwagger2
public class WebMvcConfig extends WebMvcConfigurationSupport {

	@Bean
	public MappedInterceptor myMappedInterceptor(HTTPMonitoringInterceptor interceptor) {
		String[] includeUrlPattern = new String[]{"/**"};
		// @formatter:off
		String[] excludeUrlPattern = new String[]{
				"/swagger-ui.html/**",
				"/swagger-resources/**",
				"/webjars/**",
				"/swagger-ui.html#!/**",
				"/v2/**",
				"/",
				"swagger**",
				"/actuator/**"};
		// @formatter:on
		return new MappedInterceptor(includeUrlPattern, excludeUrlPattern, interceptor);
	}

	/**
	 * add resource handlers for serving static resources
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
	}

}
