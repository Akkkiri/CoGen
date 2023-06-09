package ewha.backend.global.security.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import ewha.backend.domain.notification.repository.EmitterRepository;
import ewha.backend.global.security.cookieManager.CookieManager;
import ewha.backend.global.security.jwtTokenizer.JwtTokenizer;
import ewha.backend.global.security.refreshToken.repository.RefreshTokenQueriRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class UserLogoutHandler implements LogoutHandler {
	private final JwtTokenizer jwtTokenizer;
	private final CookieManager cookieManager;
	private final EmitterRepository emitterRepository;
	private final RefreshTokenQueriRepository refreshTokenQueriRepository;

	@SneakyThrows
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		String refreshToken = cookieManager.outCookie(request, "refreshToken");

		String userId = refreshTokenQueriRepository.findUserIdByTokenValue(refreshToken);

		emitterRepository.deleteAllEmitterStartWithId(userId);

		jwtTokenizer.removeRefreshToken(refreshToken);
		try {
			jwtTokenizer.verifySignature(refreshToken);
		} catch (ExpiredJwtException ee) {
			response.sendError(401, "Refresh Token이 만료되었습니다");
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
				response.addCookie(cookies[i]);
			}
		}

		if (authentication != null)
			new SecurityContextLogoutHandler().logout(request, response, authentication);
	}
}
