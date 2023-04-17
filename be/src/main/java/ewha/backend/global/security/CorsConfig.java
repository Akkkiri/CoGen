package ewha.backend.global.security;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_EVENT_STREAM));
		converters.add(converter);
	}
}
