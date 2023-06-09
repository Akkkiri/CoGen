package ewha.backend.global.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ewha.backend.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	// 로그인 인증 성공 시
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		log.info("# Authenticated successfully!");
	}

	public void testAop() {
		log.info("# Test Method");
	}
}
