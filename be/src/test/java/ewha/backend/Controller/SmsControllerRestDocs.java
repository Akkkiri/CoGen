// package com.ewha.back.Controller;
//
// import static com.ewha.back.Controller.constant.CommentControllerConstant.*;
// import static com.ewha.back.Controller.constant.SmsControllerConstant.*;
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.*;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.*;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.restdocs.request.RequestDocumentation.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
// import org.springframework.restdocs.payload.JsonFieldType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.ewha.back.domain.comment.dto.CommentDto;
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.user.entity.User;
// import com.ewha.back.domain.user.service.UserService;
// import com.ewha.back.global.smsAuth.dto.SmsDto;
// import com.ewha.back.global.smsAuth.service.SmsService;
// import com.google.gson.Gson;
//
// @Transactional
// @SpringBootTest
// @AutoConfigureMockMvc
// @AutoConfigureRestDocs
// public class SmsControllerRestDocs {
//
// 	@Autowired
// 	private MockMvc mockMvc;
// 	@Autowired
// 	private Gson gson;
// 	@MockBean
// 	private SmsService smsService;
// 	@MockBean
// 	private UserService userService;
//
// 	@Test
// 	void smsCertificationTest() throws Exception {
//
// 		String content = gson.toJson(NUMBER_REQUEST_DTO);
//
// 		doNothing().when(smsService).sendSms(anyString());
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/sms/send")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"SMS_Certification",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호")
// 					)
// 				)));
// 	}
//
// 	@Test
// 	void smsVerificationTest() throws Exception {
//
// 		String content = gson.toJson(SMS_CERTIFICATION_REQUEST_DTO);
//
// 		given(smsService.verifyCertification(Mockito.any(SmsDto.CertificationRequest.class))).willReturn("");
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/sms/verification")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"SMS_Verification",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호"),
// 						fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("인증 번호")
// 					)
// 				)));
// 	}
//
// 	@Test
// 	void findMyIdRequestTest() throws Exception {
//
// 		String content = gson.toJson(FIND_ID_REQUEST_DTO);
//
// 		given(userService.verifyNicknameAndPhoneNumber(anyString(), anyString())).willReturn(Boolean.TRUE);
// 		doNothing().when(smsService).sendSms(anyString());
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/find/id/sms/send")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"Find_My_Id_Request",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호")
// 					)
// 				)));
// 	}
//
// 	@Test
// 	void findMyIdVerificationTest() throws Exception {
//
// 		String content = gson.toJson(FIND_ID_CERTIFICATION_REQUEST_DTO);
//
// 		given(smsService.findVerifyCertification(Mockito.any(SmsDto.FindCertificationRequest.class)))
// 			.willReturn("");
// 		given(userService.findByNickname(anyString())).willReturn(User.builder().build());
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/find/id/sms/verification")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"Find_My_Id_Verification",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호"),
// 						fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("인증 번호")
// 					)
// 				)));
// 	}
//
// 	@Test
// 	void findMyPasswordRequestTest() throws Exception {
//
// 		String content = gson.toJson(FIND_PASSWORD_REQUEST_DTO);
//
// 		given(userService.verifyUserIdAndPhoneNumber(anyString(), anyString())).willReturn(Boolean.TRUE);
// 		doNothing().when(smsService).sendSms(anyString());
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/find/password/sms/send")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"Find_My_Password_Request",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호")
// 					)
// 				)));
// 	}
//
// 	@Test
// 	void findMyPasswordVerificationTest() throws Exception {
//
// 		String content = gson.toJson(FIND_PASSWORD_CERTIFICATION_REQUEST_DTO);
//
// 		given(smsService.findPasswordVerifyCertification(Mockito.any(SmsDto.FindPasswordCertificationRequest.class)))
// 			.willReturn("");
// 		given(userService.findByUserId(anyString())).willReturn(User.builder().build());
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders.post("/find/password/sms/verification")
// 					.accept(MediaType.APPLICATION_JSON)
// 					.contentType(MediaType.APPLICATION_JSON)
// 					.content(content)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"Find_My_Password_Verification",
// 				getDocumentRequest(),
// 				requestFields(
// 					List.of(
// 						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
// 						fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호"),
// 						fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("인증 번호")
// 					)
// 				)));
// 	}
// }
