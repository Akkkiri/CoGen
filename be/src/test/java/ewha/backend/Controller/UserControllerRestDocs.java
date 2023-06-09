package ewha.backend.Controller;

import static ewha.backend.Controller.constant.UserControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import ewha.backend.Controller.utils.WithMockCustomUser;
import ewha.backend.domain.comment.mapper.CommentMapper;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.qna.service.QnaService;
import ewha.backend.domain.question.mapper.QuestionMapper;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.security.dto.LoginDto;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private UserMapper userMapper;
	@MockBean
	private UserService userService;
	@MockBean
	private FeedMapper feedMapper;
	@MockBean
	private CommentMapper commentMapper;
	@MockBean
	private QuestionMapper questionMapper;
	@MockBean
	private AwsS3Service awsS3Service;

	@Test
	void postUserTest() throws Exception {

		String content = gson.toJson(POST_USER_DTO);

		given(userService.createUser(Mockito.any(UserDto.Post.class))).willReturn(User.builder().build());
		given(userMapper.userToUserPostResponse(Mockito.any(User.class))).willReturn(POST_USER_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/users/signup")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.userId").value(POST_USER_DTO.getUserId()))
			.andExpect(jsonPath("$.nickname").value(POST_USER_DTO.getNickname()))
			.andDo(document(
				"Post_User",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					List.of(
						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
						fieldWithPath("passwordRepeat").type(JsonFieldType.STRING).description("회원 비밀번호 확인")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath("level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath("ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
						fieldWithPath("role[]").type(JsonFieldType.ARRAY).description("회원 등급")
					)
				)));
	}

	@Test
	void firstLoginUserTest() throws Exception {

		Long userId = 1L;

		String content = gson.toJson(LOGIN_PATCH_USER_DTO);

		given(userService.onFirstLogin(anyLong(), Mockito.any(LoginDto.PatchDto.class)))
			.willReturn(User.builder().build());
		given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(USER_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/users/{user_id}/firstlogin", userId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"First_Login_User",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("genderType").type(JsonFieldType.STRING).description("회원 성별"),
						fieldWithPath("ageType").type(JsonFieldType.STRING).description("회원 연령대")
					)
				),
				responseFields(
					List.of(
						fieldWithPath(".id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath(".genderType").type(JsonFieldType.STRING).description("회원 성별"),
						fieldWithPath(".ageType").type(JsonFieldType.STRING).description("회원 연령대"),
						fieldWithPath("level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
						fieldWithPath(".role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
						fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("회원 프로필 썸네일")
					)
				)));
	}

	@Test
	void firstLoginQnaTest() throws Exception {

		Long userId = 1L;

		String content = gson.toJson(QNA_DTO_LIST);

		String result = "QnA Inserted.";

		given(userService.onFirstLoginQna(anyLong(), Mockito.anyList())).willReturn(result);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/users/{user_id}/firstqna", userId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"First_Login_QnA",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("[].qnaId").type(JsonFieldType.NUMBER).description("문답 아이디"),
						fieldWithPath("[].answerBody").type(JsonFieldType.STRING).description("문답 답변")
					)
				)));
	}

	@Test
	void updateQnaTest() throws Exception {

		String content = gson.toJson(QNA_DTO_LIST);

		String result = "QnA Updated.";

		given(userService.updateMyQna(Mockito.anyList())).willReturn(result);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/mypage/patchqna")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Update_My_QnA",
				getDocumentRequest(),
				requestFields(
					List.of(
						fieldWithPath("[].qnaId").type(JsonFieldType.NUMBER).description("문답 아이디"),
						fieldWithPath("[].answerBody").type(JsonFieldType.STRING).description("문답 답변")
					)
				)));
	}

	@Test
	@WithMockCustomUser
	void patchUserTest() throws Exception {

		String content = gson.toJson(PATCH_USER_DTO);

		given(userService.updateUser(Mockito.any(UserDto.UserInfo.class))).willReturn(User.builder().build());
		given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(USER_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/mypage/patch")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value(PATCH_USER_DTO.getNickname()))
			.andDo(document(
				"Patch_User",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					List.of(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath("genderType").type(JsonFieldType.STRING).description("회원 성별"),
						fieldWithPath("ageType").type(JsonFieldType.STRING).description("회원 연령대"),
						fieldWithPath("profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
					)
				),
				responseFields(
					List.of(
						fieldWithPath(".id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath(".genderType").type(JsonFieldType.STRING).description("회원 성별"),
						fieldWithPath(".ageType").type(JsonFieldType.STRING).description("회원 연령대"),
						fieldWithPath(".level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
						fieldWithPath(".role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
						fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("회원 프로필 썸네일")
					)
				)));
	}

	// @Test
	// @WithMockCustomUser
	// void patchUserTest() throws Exception {
	//
	// 	String content = gson.toJson(PATCH_USER_DTO);
	//
	// 	MockMultipartFile json =
	// 		new MockMultipartFile("patch", "dto",
	// 			"application/json", content.getBytes(StandardCharsets.UTF_8));
	//
	// 	MockMultipartFile image =
	// 		new MockMultipartFile("image", "image.png",
	// 			"image/png", "<<png data>>".getBytes());
	//
	// 	given(userService.getLoginUser()).willReturn(User.builder().build());
	// 	given(userService.updateUser(Mockito.any(UserDto.UserInfo.class))).willReturn(User.builder().build());
	// 	given(awsS3Service.uploadImageToS3(Mockito.any(MultipartFile.class), anyLong()))
	// 		.willReturn(new ArrayList<>());
	// 	doNothing().when(userService).saveUser(Mockito.any(User.class));
	// 	given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(USER_RESPONSE_DTO);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.multipart("/api/mypage/patch")
	// 				.file(json)
	// 				.file(image)
	// 				.contentType(MediaType.MULTIPART_FORM_DATA)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 				.content(content)
	// 		);
	//
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.nickname").value(PATCH_USER_DTO.getNickname()))
	// 		.andDo(document(
	// 			"Patch_User",
	// 			getDocumentRequest(),
	// 			getDocumentResponse(),
	// 			requestFields(
	// 				List.of(
	// 					fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
	// 					fieldWithPath("genderType").type(JsonFieldType.STRING).description("회원 성별"),
	// 					fieldWithPath("ageType").type(JsonFieldType.STRING).description("회원 연령대"),
	// 					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
	// 				)
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath(".id").type(JsonFieldType.NUMBER).description("회원 번호"),
	// 					fieldWithPath(".userId").type(JsonFieldType.STRING).description("회원 아이디"),
	// 					fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
	// 					fieldWithPath(".hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
	// 					fieldWithPath(".genderType").type(JsonFieldType.STRING).description("회원 성별"),
	// 					fieldWithPath(".ageType").type(JsonFieldType.STRING).description("회원 연령대"),
	// 					fieldWithPath(".level").type(JsonFieldType.NUMBER).description("회원 레벨"),
	// 					fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
	// 					fieldWithPath(".role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
	// 					fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
	// 					fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("회원 프로필 썸네일")
	// 				)
	// 			)));
	// }

	@Test
	void patchPasswordTest() throws Exception {

		String content = gson.toJson(CHANGE_PASSWORD_DTO);

		doNothing().when(userService).updatePassword(Mockito.any(UserDto.Password.class));

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/mypage/patch/password")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Patch_Password",
				getDocumentRequest(),
				requestFields(
					List.of(
						fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새로운 비밀번호"),
						fieldWithPath("newPasswordRepeat").type(JsonFieldType.STRING).description("비밀번호 확인")
					)
				)));
	}

	@Test
	void getMyPageTest() throws Exception {

		given(userService.getMyInfo()).willReturn(User.builder().build());
		given(userMapper.userToMyPageResponse(Mockito.any(User.class))).willReturn(MY_PAGE_INFO_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(MY_PAGE_INFO_RESPONSE_DTO.getUserId()))
			.andDo(document(
				"Get_MyPage",
				getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath(".userId").type(JsonFieldType.STRING).description("유저 아이디"),
						fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath(".friendsNum").type(JsonFieldType.NUMBER).description("친구 숫자"),
						fieldWithPath(".level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
						fieldWithPath(".genderType").type(JsonFieldType.STRING).description("성별"),
						fieldWithPath(".ageType").type(JsonFieldType.STRING).description("연령대"),
						fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로")
					)
				)));
	}

	@Test
	void getMyQnaTest() throws Exception {

		given(userService.getLoginUser()).willReturn(User.builder().build());
		given(userMapper.userToQnaResponse(any(User.class), any(QnaService.class))).willReturn(MY_QNA_RESPONSE_LIST);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage/myqna")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_My_QnA",
				getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath("[].qnaId").type(JsonFieldType.NUMBER).description("문답 번호"),
						fieldWithPath("[].content").type(JsonFieldType.STRING).description("문답 내용"),
						fieldWithPath("[].answerBody").type(JsonFieldType.STRING).description("문답 답변")
					)
				)));
	}

	@Test
	void getMyQuestionsTest() throws Exception {

		int page = 1;

		given(userService.findMyQuestions(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(questionMapper.myQuestionsToPageResponse(Mockito.any(Page.class), Mockito.any(UserService.class)))
			.willReturn(QUESTION_RESPONSE_PAGE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage/myquestions?page={page}", page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_My_Questions_Comments",
				getDocumentResponse(),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
						fieldWithPath("data[].content").type(JsonFieldType.STRING).description("질문 내용"),
						fieldWithPath("data[].answerList").type(JsonFieldType.OBJECT).description("답변 리스트"),
						fieldWithPath("data[].answerList.answerBody[]").type(JsonFieldType.ARRAY).description("답변 내용"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 질문 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getMyBookmarksTest() throws Exception {

		int page = 1;

		given(userService.findMyBookmark(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(feedMapper.myBookmarksToPageResponse(Mockito.any(Page.class)))
			.willReturn(FEED_PAGE_RESPONSES);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage/bookmarks?page={page}", page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_My_Bookmarks",
				getDocumentResponse(),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath("data[].category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath("data[].title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath("data[].body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath("data[].userId").type(JsonFieldType.STRING).description("작성자 번호"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
						fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
						fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER).description("조회수"),
						fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
						fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 시간"),
						fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 시간"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 북마크 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getMyFeedsTest() throws Exception {

		int page = 1;

		given(userService.findMyFeeds(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(feedMapper.myFeedsToPageResponse(Mockito.any())).willReturn(USER_FEED_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage/myfeeds?page={page}", page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_My_Feeds",
				getDocumentResponse(),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath(".data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath(".data[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
						fieldWithPath(".data[].hashcode").type(JsonFieldType.STRING).description("작성자 해시코드"),
						fieldWithPath(".data[].title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath(".data[].body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath(".data[].category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath(".data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
						fieldWithPath(".data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
						fieldWithPath(".data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
						fieldWithPath(".data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getMyCommentsTest() throws Exception {

		int page = 1;

		given(userService.findUserComments(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(commentMapper.myCommentsToPageResponse(any(), any(LikeService.class))).willReturn(
			USER_COMMENT_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/mypage/mycomments?page={page}", page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_My_Comments",
				getDocumentResponse(),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
						fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
						fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
						fieldWithPath("data[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로"),
						fieldWithPath("data[].body").type(JsonFieldType.STRING).description("댓글 내용"),
						fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
						fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요"),
						fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 댓글 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getUserPageTest() throws Exception {

		Long userId = 1L;

		given(userService.getUser(anyLong())).willReturn(User.builder().build());
		given(userMapper.userToUserPageResponse(Mockito.any(User.class), anyBoolean()))
			.willReturn(USER_PAGE_INFO_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{user_id}", userId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(USER_PAGE_INFO_RESPONSE_DTO.getUserId()))
			.andDo(document(
				"Get_User_Page",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				responseFields(
					List.of(
						fieldWithPath(".userId").type(JsonFieldType.STRING).description("유저 아이디"),
						fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath(".isFollowing").type(JsonFieldType.BOOLEAN).description("친구신청 여부"),
						fieldWithPath(".friendsNum").type(JsonFieldType.NUMBER).description("친구 숫자"),
						fieldWithPath(".level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("코젠지수"),
						fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로")
					)
				)));
	}

	@Test
	void getUserQnaTest() throws Exception {

		Long userId = 1L;

		given(userService.getUser(anyLong())).willReturn(User.builder().build());
		given(userMapper.userToQnaResponse(any(User.class), any(QnaService.class))).willReturn(USER_QNA_RESPONSE_LIST);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{user_id}/qna", userId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_User_QnA",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("[].qnaId").type(JsonFieldType.NUMBER).description("문답 번호"),
						fieldWithPath("[].content").type(JsonFieldType.STRING).description("문답 내용"),
						fieldWithPath("[].answerBody").type(JsonFieldType.STRING).description("문답 답변")
					)
				)));
	}

	@Test
	void getUserQuestionsTest() throws Exception {

		Long userId = 1L;
		int page = 1;

		given(userService.findUserQuestions(anyLong(), anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(questionMapper.userQuestionsToPageResponse(
			Mockito.any(Page.class), Mockito.any(UserService.class), anyLong()))
			.willReturn(QUESTION_RESPONSE_PAGE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{user_id}/questions?page={page}", userId, page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_User_Questions_Comments",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
						fieldWithPath("data[].content").type(JsonFieldType.STRING).description("질문 내용"),
						fieldWithPath("data[].answerList").type(JsonFieldType.OBJECT).description("답변 리스트"),
						fieldWithPath("data[].answerList.answerBody[]").type(JsonFieldType.ARRAY).description("답변 내용"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 질문 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getUserFeedsTest() throws Exception {

		Long userId = 1L;
		int page = 1;

		given(userService.findUserFeeds(anyLong(), anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(feedMapper.userFeedsToPageResponse(Mockito.any())).willReturn(FEED_PAGE_RESPONSES);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{user_id}/feeds?page={page}", userId, page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_User_Feeds",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("user_id").description("유저 번호")
				),
				requestParameters(
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath(".data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath(".data[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
						fieldWithPath(".data[].title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath(".data[].body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath(".data[].category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath(".data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
						fieldWithPath(".data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
						fieldWithPath(".data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
						fieldWithPath(".data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	// @Test
	// void getUserPageTest() throws Exception {
	//
	// 	Long userId = 1L;
	//
	// 	given(userService.getUser(anyLong())).willReturn(User.builder().build());
	// 	given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(USER_RESPONSE_DTO);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/users/{user_id}", userId)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	//
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.userId").value(USER_RESPONSE_DTO.getUserId()))
	// 		.andDo(document(
	// 			"Get_User_Page",
	// 			getDocumentResponse(),
	// 			pathParameters(
	// 				parameterWithName("user_id").description("유저 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath(".id").type(JsonFieldType.NUMBER).description("회원 번호"),
	// 					fieldWithPath(".userId").type(JsonFieldType.STRING).description("회원 아이디"),
	// 					fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
	// 					fieldWithPath(".genderType").type(JsonFieldType.STRING).description("회원 성별"),
	// 					fieldWithPath(".ageType").type(JsonFieldType.STRING).description("회원 연령대"),
	// 					fieldWithPath(".categoryTypes").type(JsonFieldType.ARRAY).description("회원 연령대"),
	// 					fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath(".role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
	// 					fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
	// 					fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("회원 프로필 썸네일")
	// 				)
	// 			)));
	// }
	//
	// @Test
	// void getMyPageTest() throws Exception {
	//
	// 	given(userService.getMyInfo()).willReturn(User.builder().build());
	// 	given(userMapper.userToUserInfoResponse(Mockito.any(User.class))).willReturn(MY_PAGE_INFO_RESPONSE_DTO);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/mypage")
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	//
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.userId").value(MY_PAGE_INFO_RESPONSE_DTO.getUserId()))
	// 		.andDo(document(
	// 			"Get_MyPage",
	// 			getDocumentResponse(),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath(".userId").type(JsonFieldType.STRING).description("유저 아이디"),
	// 					fieldWithPath(".nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
	// 					fieldWithPath(".introduction").type(JsonFieldType.STRING).description("회원 자기 소개"),
	// 					fieldWithPath(".genderType").type(JsonFieldType.STRING).description("회원 성별"),
	// 					fieldWithPath(".ageType").type(JsonFieldType.STRING).description("회원 연령대"),
	// 					fieldWithPath(".ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath(".profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
	// 					fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로"),
	// 					fieldWithPath(".phoneNumber").type(JsonFieldType.STRING).description("회원 휴대폰 번호"),
	// 					fieldWithPath(".birthday").type(JsonFieldType.STRING).description("생일"),
	// 					fieldWithPath(".centerCode").type(JsonFieldType.NUMBER).description("센터 코드"),
	// 					fieldWithPath(".isFirstLogin").type(JsonFieldType.BOOLEAN).description("첫 로그인 여부"),
	// 					fieldWithPath(".categories[]").type(JsonFieldType.ARRAY).description("회원 관심사")
	// 				)
	// 			)));
	//
	// }
	//
	// @Test
	// void getUserFeedsTest() throws Exception {
	//
	// 	int page = 1;
	//
	// 	given(userService.findMyFeeds(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
	// 	given(feedMapper.myFeedsToPageResponse(Mockito.any())).willReturn(USER_FEED_RESPONSE_PAGE);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/mypage/myfeeds?page={page}", page)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Get_User_Feeds",
	// 			getDocumentResponse(),
	// 			requestParameters(
	// 				parameterWithName("page").description("페이지 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
	// 					fieldWithPath(".data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath(".data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath(".data[].title").type(JsonFieldType.STRING).description("피드 제목"),
	// 					// fieldWithPath(".data[].body").type(JsonFieldType.STRING).description("피드 내용"),
	// 					fieldWithPath(".data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
	// 					fieldWithPath(".data[].categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
	// 					fieldWithPath(".data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
	// 					fieldWithPath(".data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
	// 					fieldWithPath(".data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath(".data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
	// 					fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
	// 					fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
	// 					fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
	// 					fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
	// 					fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
	// 				)
	// 			)));
	//
	// }
	//
	// @Test
	// void getUserCommentsTest() throws Exception {
	//
	// 	int page = 1;
	//
	// 	given(userService.findUserComments(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
	// 	given(commentMapper.myCommentsToPageResponse(Mockito.any())).willReturn(USER_COMMENT_RESPONSE_PAGE);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/mypage/mycomments?page={page}", page)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Get_User_Comments",
	// 			getDocumentResponse(),
	// 			requestParameters(
	// 				parameterWithName("page").description("페이지 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
	// 					fieldWithPath("data[].commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
	// 					fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data[].body").type(JsonFieldType.STRING).description("댓글 내용"),
	// 					fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
	// 					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
	// 					fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
	// 					fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
	// 					fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
	// 					fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
	// 				)
	// 			)));
	//
	// }
	//
	// @Test
	// void getUserLikedFeedsTest() throws Exception {
	//
	// 	int page = 1;
	//
	// 	given(userService.findUserLikedFeed(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
	// 	given(feedMapper.myFeedsToPageResponse(Mockito.any())).willReturn(USER_FEED_RESPONSE_PAGE);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/mypage/mylikedfeeds?page={page}", page)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Get_User_Liked_Feeds",
	// 			getDocumentResponse(),
	// 			requestParameters(
	// 				parameterWithName("page").description("페이지 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
	// 					fieldWithPath(".data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath(".data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath(".data[].title").type(JsonFieldType.STRING).description("피드 제목"),
	// 					// fieldWithPath(".data[].body").type(JsonFieldType.STRING).description("피드 내용"),
	// 					fieldWithPath(".data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
	// 					fieldWithPath(".data[].categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
	// 					fieldWithPath(".data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
	// 					fieldWithPath(".data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
	// 					fieldWithPath(".data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath(".data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
	// 					fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
	// 					fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
	// 					fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
	// 					fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
	// 					fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
	// 				)
	// 			)));
	//
	// }
	//
	// @Test
	// void getUserQuestionsTest() throws Exception {
	//
	// 	int page = 1;
	//
	// 	given(userService.findUserQuestions(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
	// 	given(questionMapper.myQuestionsToPageResponse(Mockito.any())).willReturn(USER_ANSWERED_QUESTION_PAGE);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/mypage/myquestions?page={page}", page)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Get_User_Questions",
	// 			getDocumentResponse(),
	// 			requestParameters(
	// 				parameterWithName("page").description("페이지 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
	// 					fieldWithPath(".data[].questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
	// 					fieldWithPath(".data[].title").type(JsonFieldType.STRING).description("질문 제목"),
	// 					fieldWithPath(".data[].body").type(JsonFieldType.STRING).description("질문 내용"),
	// 					fieldWithPath(".data[].imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
	// 					fieldWithPath(".data[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로"),
	// 					fieldWithPath(".data[].answerBody").type(JsonFieldType.STRING).description("정답"),
	// 					fieldWithPath(".data[].userAnswer").type(JsonFieldType.STRING).description("사용자 답변"),
	// 					fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
	// 					fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
	// 					fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
	// 					fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
	// 				)
	// 			)));
	// }
	//
	@Test
	void deleteUserTest() throws Exception {

		doNothing().when(userService).deleteUser();

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/mypage/signout")
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_User",
					getDocumentRequest(),
					getDocumentResponse()
				)
			);
	}

}
