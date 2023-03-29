package ewha.backend.Controller;

import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;
import ewha.backend.global.security.dto.LoginDto;
import ewha.backend.global.security.service.UserDetailsServiceImpl;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SecurityConfigRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@Test
	void loginTest() throws Exception {

		LoginDto.PostDto post =
			LoginDto.PostDto.builder()
				.userId("01012345678")
				.password("12345678a")
				.build();

		String content = gson.toJson(post);

		User user =
			User.builder()
				.id(1L)
				.userId("01012345678")
				.isFirstLogin(false)
				.password(bCryptPasswordEncoder.encode("12345678a"))
				.role(List.of("ROLE_USER"))
				.nickname("닉네임")
				.level(1)
				.ariFactor(10)
				.role(List.of("ROLE_USER"))
				.genderType(GenderType.FEMALE)
				.ageType(AgeType.THIRTIES)
				.build();

		UserDetailsServiceImpl.UserDetailsImpl userDetails = new UserDetailsServiceImpl.UserDetailsImpl(user);

		given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/login")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(post.getUserId()))
			.andDo(document(
				"User_Login",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					List.of(
						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 넘버"),
						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath("isFirstLogin").type(JsonFieldType.BOOLEAN).description("최초 로그인 여부"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath("ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
						fieldWithPath("role[]").type(JsonFieldType.ARRAY).description("회원 등급")
					)
				)
			));
	}

}
