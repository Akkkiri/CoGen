package ewha.backend.global.security.oauth.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.security.util.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NaverService {

	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String NAVER_TOKEN_URI;
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String NAVER_CLIENT_ID;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String NAVER_CLIENT_SECRET;
	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String NAVER_REDIRECT_URI;
	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String NAVER_USER_INFO_URI;

	private final UserService userService;
	private final UserRepository userRepository;
	private final CustomAuthorityUtils customAuthorityUtils;
	private final PasswordEncoder passwordEncoder;

	public User doFilter(String authorize_code) {

		String accessToken = "";
		String refreshToken = "";

		try {
			URL url = new URL(NAVER_TOKEN_URI);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			//    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			//    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("content_type:" + "application/x-www-form-urlencoded");
			sb.append("&grant_type=authorization_code");
			sb.append("&client_id=").append(NAVER_CLIENT_ID);
			sb.append("&client_secret=").append(NAVER_CLIENT_SECRET);
			sb.append("&redirect_uri=").append(NAVER_REDIRECT_URI);
			sb.append("&client_name=Naver");
			//            sb.append("&client_secret=Y4aPCredJvfOGMtsTZHT2i50nX3EyvZ7");
			sb.append("&code=").append(authorize_code);
			bw.write(sb.toString());
			bw.flush();

			// 결과 코드가 200이면 성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder result = new StringBuilder();
			String line = "";

			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			System.out.println("response body : " + result);

			// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
			JsonElement element = JsonParser.parseString(result.toString());

			accessToken = element.getAsJsonObject().get("access_token").getAsString();
			refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

			System.out.println("access_token : " + accessToken);
			System.out.println("refresh_token : " + refreshToken);

			br.close();
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getUserInfo(accessToken);
		// return accessToken;
	}

	public User getUserInfo(String accessToken) {

		// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
		HashMap<String, Object> userInfo = new HashMap<>();
		try {
			URL url = new URL(NAVER_USER_INFO_URI);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");

			// 요청에 필요한 Header에 포함될 내용
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder result = new StringBuilder();
			String line = "";

			while ((line = br.readLine()) != null) {
				result.append(line);
			}

			JsonElement element = JsonParser.parseString(result.toString());

			JsonObject properties = element.getAsJsonObject().get("response").getAsJsonObject();

			String providerId = properties.getAsJsonObject().get("id").getAsString();
			String nickname = properties.getAsJsonObject().get("nickname").getAsString();
			String picture = properties.getAsJsonObject().get("profile_image").getAsString();
			String email = properties.getAsJsonObject().get("email").getAsString();

			userInfo.put("providerId", providerId);
			userInfo.put("nickname", nickname);
			userInfo.put("profile_image", picture);
			userInfo.put("email", email);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return createUser(userInfo);
		// return userInfo;
	}

	public User createUser(HashMap<String, Object> userInfo) { // OAuth 인증이 끝나 유저 정보를 받은 경우

		String uuid = UUID.randomUUID().toString().substring(0, 8);
		String userId = createUserId(); // 사용자가 입력한 적은 없지만 만들어준다

		String providerId = userInfo.get("providerId").toString();

		String email = userInfo.get("email").toString();
		String picture = userInfo.get("profile_image").toString();
		String nickname = userInfo.get("nickname").toString();
		String encodedPass = passwordEncoder.encode(userInfo.get("nickname").toString());

		User findUser = userRepository.findByProviderId(providerId);

		if (findUser != null && findUser.getProvider().equals("NAVER")) {
			// findUser.updateOAuthInfo(email, picture, nickname);
			// return userRepository.save(findUser);
			return findUser;
		}
		// else if (findUser == null && userRepository.findByEmail(email).isPresent()) {
		// 	throw new BusinessLogicException(ExceptionCode.EMAIL_USED_ANOTHER_ACCOUNT);
		// }

		User.UserBuilder userBuilder = User.builder();

		userBuilder.userId(userId);

		if (!nickname.matches("[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}")){
			nickname = "NAVERUSER";
		}

		userBuilder.nickname(userService.createNickname(nickname));
		// userBuilder.email(email);

		// if (userRepository.findByNickname(nickname) == null) {
		// 	userBuilder.nickname(nickname);
		// } else {
		// 	String rand = UUID.randomUUID().toString().substring(0, 6);
		// 	userBuilder.nickname(nickname + "_NAVER_" + rand);
		// }

		userBuilder.profileImage(picture);
		userBuilder.role(customAuthorityUtils.createRoles(userId));
		userBuilder.password(encodedPass);
		userBuilder.level(1);
		userBuilder.ariFactor(10);
		userBuilder.isFirstLogin(true);
		userBuilder.provider("NAVER");
		userBuilder.providerId(providerId);

		return userRepository.save(userBuilder.build());
	}

	private String createUserId() {

		Random rand = new Random();
		StringBuilder userId = new StringBuilder();
		userId.append("222");
		for (int i = 0; i < 8; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			userId.append(ran);
		}

		while (userRepository.existsByUserId(userId.toString())) {
			StringBuilder temp = new StringBuilder();
			temp.append("222");
			for (int i = 0; i < 8; i++) {
				String ran = Integer.toString(rand.nextInt(10));
				temp.append(ran);
			}
			userId = temp;
		}
		return userId.toString();
	}
}
