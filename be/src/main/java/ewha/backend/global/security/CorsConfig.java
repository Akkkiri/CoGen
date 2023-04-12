package ewha.backend.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			// .allowedOrigins("*")
			.allowedOrigins("http://localhost:3000", "https://www.akkkiri.co.kr/", "https://183.96.15.99/",
				"https://akkkiri.co.kr/", "https://api.akkkiri.co.kr/", "http://localhost:8080")
			.allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
			.exposedHeaders("Authorization")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
