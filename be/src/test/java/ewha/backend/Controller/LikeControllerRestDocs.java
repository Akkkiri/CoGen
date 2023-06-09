package ewha.backend.Controller;

import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.comment.mapper.CommentMapper;
import ewha.backend.domain.comment.service.CommentService;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.user.mapper.UserMapper;
import com.google.gson.Gson;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class LikeControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private LikeService likeService;
	@MockBean
	private FeedService feedService;
	@MockBean
	private CommentService commentService;
	@MockBean
	private UserMapper userMapper;
	@MockBean
	private FeedMapper feedMapper;
	@MockBean
	private CommentMapper commentMapper;

	@Test
	void feedLikeTest() throws Exception {

		Long feedId = 1L;

		given(likeService.feedLike(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/feeds/{feed_id}/like", feedId)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Feed_Like",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				)
				));
	}

	@Test
	void commentLikeTest() throws Exception {

		Long commentId = 1L;

		given(likeService.commentLike(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/comments/{comment_id}/like", commentId)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Comment_Like",
				pathParameters(
					parameterWithName("comment_id").description("댓글 번호")
				)
				));
	}

	@Test
	void answerLikeTest() throws Exception {

		Long answerId = 1L;

		given(likeService.answerLike(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/answers/{answer_id}/like", answerId)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Answer_Like",
				pathParameters(
					parameterWithName("answer_id").description("답변 번호")
				)
			));
	}

	// @Test
	// void postFeedLikeTest() throws Exception {
	//
	// 	Long feedId = 1L;
	//
	// 	FeedDto.Response response =
	// 		FeedDto.Response.builder()
	// 			.feedId(1L)
	// 			.categories(List.of(CategoryType.CULTURE))
	// 			.userInfo(userMapper.userToUserPostResponse(new User()))
	// 			.title("피드 제목")
	// 			.body("피드 내용")
	// 			.isLiked(false)
	// 			.likeCount(1L)
	// 			.viewCount(1L)
	// 			.imagePath("이미지 주소")
	// 			.comments(new ArrayList<>())
	// 			.createdAt(LocalDateTime.now())
	// 			.modifiedAt(LocalDateTime.now())
	// 			.build();
	//
	// 	given(feedService.findVerifiedFeed(anyLong())).willReturn(Feed.builder().build());
	// 	given(likeService.createFeedLike(anyLong())).willReturn(Feed.builder().build());
	// 	given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.patch("/feeds/{feed_id}/like", feedId)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.data.title").value(response.getTitle()))
	// 		.andDo(document(
	// 			"Post_Feed_Like",
	// 			getDocumentRequest(),
	// 			getDocumentResponse(),
	// 			pathParameters(
	// 				parameterWithName("feed_id").description("피드 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
	// 					fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
	// 					fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
	// 					fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
	// 					fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
	// 						.description("작성자 프로필 사진"),
	// 					fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
	// 					fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
	// 					fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
	// 					fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("증가된 피드 좋아요"),
	// 					fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
	// 					fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
	// 					fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
	// 					fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
	// 				)
	// 			)));
	// }
	//
	// @Test
	// void deleteFeedLikeTest() throws Exception {
	//
	// 	Long feedId = 1L;
	//
	// 	FeedDto.Response response =
	// 		FeedDto.Response.builder()
	// 			.feedId(1L)
	// 			.categories(List.of(CategoryType.CULTURE))
	// 			.userInfo(userMapper.userToUserPostResponse(new User()))
	// 			.title("피드 제목")
	// 			.body("피드 내용")
	// 			.isLiked(false)
	// 			.likeCount(0L)
	// 			.viewCount(1L)
	// 			.imagePath("이미지 주소")
	// 			.comments(new ArrayList<>())
	// 			.createdAt(LocalDateTime.now())
	// 			.modifiedAt(LocalDateTime.now())
	// 			.build();
	//
	// 	given(likeService.deleteFeedLike(anyLong())).willReturn(Feed.builder().build());
	// 	given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.patch("/feeds/{feed_id}/dislike", feedId)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.data.title").value(response.getTitle()))
	// 		.andDo(document(
	// 			"Delete_Feed_Like",
	// 			getDocumentRequest(),
	// 			getDocumentResponse(),
	// 			pathParameters(
	// 				parameterWithName("feed_id").description("피드 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
	// 					fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
	// 					fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
	// 					fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
	// 					fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
	// 						.description("작성자 프로필 사진"),
	// 					fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
	// 					fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
	// 					fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
	// 					fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("감소된 피드 좋아요"),
	// 					fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
	// 					fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
	// 					fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
	// 					fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
	// 				)
	// 			)));
	// }
	//
	// @Test
	// void postCommentLikeTest() throws Exception {
	//
	// 	Long commentId = 1L;
	//
	// 	CommentDto.Response response =
	// 		CommentDto.Response.builder()
	// 			.commentId(1L)
	// 			.feedId(1L)
	// 			.userInfo(userMapper.userToUserPostResponse(new User()))
	// 			.body("댓글 내용")
	// 			.likeCount(1L)
	// 			.createdAt(LocalDateTime.now())
	// 			.modifiedAt(LocalDateTime.now())
	// 			.build();
	//
	// 	given(commentService.findVerifiedComment(anyLong())).willReturn(Comment.builder().build());
	// 	given(likeService.createCommentLike(anyLong())).willReturn(Comment.builder().build());
	// 	given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.patch("/comments/{comment_id}/like", commentId)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Post_Comment_Like",
	// 			getDocumentRequest(),
	// 			getDocumentResponse(),
	// 			pathParameters(
	// 				parameterWithName("comment_id").description("댓글 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
	// 					fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
	// 					fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
	// 					fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
	// 					fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
	// 						.description("작성자 프로필 사진"),
	// 					fieldWithPath("data.body").type(JsonFieldType.STRING).description("댓글 내용"),
	// 					fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("증가된 댓글 좋아요"),
	// 					fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
	// 				)
	// 			)));
	// }
	//
	// @Test
	// void deleteCommentLikeTest() throws Exception {
	//
	// 	Long commentId = 1L;
	//
	// 	CommentDto.Response response =
	// 		CommentDto.Response.builder()
	// 			.commentId(1L)
	// 			.feedId(1L)
	// 			.userInfo(userMapper.userToUserPostResponse(new User()))
	// 			.body("댓글 내용")
	// 			.likeCount(0L)
	// 			.createdAt(LocalDateTime.now())
	// 			.modifiedAt(LocalDateTime.now())
	// 			.build();
	//
	// 	given(likeService.deleteCommentLike(anyLong())).willReturn(Comment.builder().build());
	// 	given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.patch("/comments/{comment_id}/dislike", commentId)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Delete_Comment_Like",
	// 			getDocumentRequest(),
	// 			getDocumentResponse(),
	// 			pathParameters(
	// 				parameterWithName("comment_id").description("댓글 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
	// 					fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
	// 					fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
	// 					fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
	// 					fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
	// 					fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
	// 						.description("작성자 프로필 사진"),
	// 					fieldWithPath("data.body").type(JsonFieldType.STRING).description("댓글 내용"),
	// 					fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("감소된 댓글 좋아요"),
	// 					fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
	// 				)
	// 			)));
	// }
}
