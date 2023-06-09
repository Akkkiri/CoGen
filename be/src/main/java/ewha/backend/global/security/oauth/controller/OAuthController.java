package ewha.backend.global.security.oauth.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.global.security.cookieManager.CookieManager;
import ewha.backend.global.security.dto.LoginDto;
import ewha.backend.global.security.jwtTokenizer.JwtTokenizer;
import ewha.backend.global.security.oauth.service.KakaoService;
import ewha.backend.global.security.oauth.service.NaverService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {

	private final KakaoService kakaoService;
	private final NaverService naverService;
	private final UserMapper userMapper;
	private final CookieManager cookieManager;
	private final JwtTokenizer jwtTokenizer;

	@ResponseBody
	@GetMapping("/{provider}")
	public ResponseEntity<LoginDto.ResponseDto> oAuthCallback(
		@PathVariable("provider") String providerId,
		@RequestParam("code") String code, HttpServletResponse httpServletResponse) {

		User findUser = User.builder().build();

		switch (providerId) {

			case "kakao":
				findUser = kakaoService.doFilter(code);
				break;

			case "naver":
				findUser = naverService.doFilter(code);
				break;
		}

		String accessToken = jwtTokenizer.delegateAccessToken(findUser);
		String refreshToken = jwtTokenizer.delegateRefreshToken(findUser);

		jwtTokenizer.addRefreshToken(findUser.getUserId(), refreshToken);

		ResponseCookie cookie = cookieManager.createCookie("refreshToken", refreshToken);

		httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);
		httpServletResponse.setHeader("Set-Cookie", cookie.toString());

		LoginDto.ResponseDto response = userMapper.userToLoginResponse(findUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
