package ewha.backend.global.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.global.security.cookieManager.CookieManager;
import ewha.backend.global.security.filter.JwtAuthenticationFilter;
import ewha.backend.global.security.filter.JwtVerificationFilter;
import ewha.backend.global.security.handler.UserAccessDeniedHandler;
import ewha.backend.global.security.handler.UserAuthenticationEntryPoint;
import ewha.backend.global.security.handler.UserAuthenticationFailureHandler;
import ewha.backend.global.security.handler.UserAuthenticationSuccessHandler;
import ewha.backend.global.security.handler.UserLogoutHandler;
import ewha.backend.global.security.handler.UserLogoutSuccessHandler;
import ewha.backend.global.security.jwtTokenizer.JwtTokenizer;
import ewha.backend.global.security.util.CustomAuthorityUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	private final JwtTokenizer jwtTokenizer;
	private final CustomAuthorityUtils authorityUtils;
	private final UserMapper userMapper;
	private final CookieManager cookieManager;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().sameOrigin()
			.and()
			.csrf().disable()
			.cors(withDefaults())
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.httpBasic().disable()
			.exceptionHandling()
			.authenticationEntryPoint(new UserAuthenticationEntryPoint())
			.accessDeniedHandler(new UserAccessDeniedHandler())
			.and()
			.apply(new CustomFilterConfigurer())
			.and()
			.logout()
			.logoutUrl("/logout")
			.addLogoutHandler(new UserLogoutHandler(jwtTokenizer, cookieManager))
			.logoutSuccessHandler(new UserLogoutSuccessHandler())
			.deleteCookies("refreshToken")
			.deleteCookies("visit_cookie")
			.and()
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest().permitAll());

		return http.build();
	}

	// CORS 정책은 corsConfig에서 설정

	public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
		@Override
		public void configure(HttpSecurity builder) throws Exception {
			AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

			JwtAuthenticationFilter jwtAuthenticationFilter =
				new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, userMapper, cookieManager);
			jwtAuthenticationFilter.setFilterProcessesUrl("/login");
			jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
			jwtAuthenticationFilter.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());

			JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

			builder.addFilter(jwtAuthenticationFilter)
				.addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
		}
	}
}
