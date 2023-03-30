package ewha.backend.Controller;

import static ewha.backend.Controller.constant.CommentControllerConstant.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.mapper.CommentMapper;
import ewha.backend.domain.comment.service.CommentService;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.user.mapper.UserMapper;

import com.google.gson.Gson;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class CommentControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private UserMapper userMapper;
	@MockBean
	private CommentMapper commentMapper;
	@MockBean
	private CommentService commentService;
	@MockBean
	private LikeService likeService;

	@Test
	void postCommentTest() throws Exception {

		Long feedId = 1L;

		String content = gson.toJson(POST_COMMENT_DTO);

		given(commentMapper.commentPostToComment(Mockito.any(CommentDto.Post.class)))
			.willReturn(Comment.builder().build());
		given(commentService.createComment(Mockito.any(Comment.class), anyLong()))
			.willReturn(Comment.builder().build());
		given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(COMMENT_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/feeds/{feed_id}/comments/add", feedId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body").value(POST_COMMENT_DTO.getBody()))
			.andDo(document(
				"Post_Comment",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath(".commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
						fieldWithPath(".feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".userInfo.id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".userInfo.userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".userInfo.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".userInfo.level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".userInfo.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".userInfo.thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".body").type(JsonFieldType.STRING).description("댓글 내용"),
						fieldWithPath(".likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요"),
						fieldWithPath(".createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					)
				)));
	}

	@Test
	void patchCommentTest() throws Exception {

		Long commentId = 1L;

		String content = gson.toJson(PATCH_COMMENT_DTO);

		given(commentMapper.commentPatchToComment(Mockito.any(CommentDto.Patch.class)))
			.willReturn(Comment.builder().build());
		given(commentService.updateComment(Mockito.any(Comment.class), anyLong()))
			.willReturn(Comment.builder().build());
		given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(COMMENT_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/comments/{comment_id}/edit", commentId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body").value(PATCH_COMMENT_DTO.getBody()))
			.andDo(document(
				"Patch_Comment",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("comment_id").description("댓글 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("body").type(JsonFieldType.STRING).description("수정된 댓글 내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath(".commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
						fieldWithPath(".feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".userInfo.id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".userInfo.userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".userInfo.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".userInfo.level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".userInfo.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".userInfo.thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".body").type(JsonFieldType.STRING).description("댓글 내용"),
						fieldWithPath(".likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요"),
						fieldWithPath(".createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					)
				)));
	}

	@Test
	void getCommentTest() throws Exception {

		Long commentId = 1L;

		given(commentService.findVerifiedComment(anyLong())).willReturn(Comment.builder().build());
		given(likeService.isLikedComment(Mockito.any(Comment.class))).willReturn(Boolean.TRUE);
		given(commentMapper.commentToCommentGetResponse(Mockito.any(Comment.class), anyBoolean()))
			.willReturn(GET_COMMENT_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/comments/{comment_id}", commentId)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Comment",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("comment_id").description("댓글 번호")
				),
				responseFields(
					List.of(
						fieldWithPath(".commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
						fieldWithPath(".feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".userInfo.id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".userInfo.userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".userInfo.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".userInfo.level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".userInfo.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
						fieldWithPath(".userInfo.thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".body").type(JsonFieldType.STRING).description("댓글 내용"),
						fieldWithPath(".isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
						fieldWithPath(".likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요"),
						fieldWithPath(".reportCount").type(JsonFieldType.NUMBER).description("댓글 신고 개수"),
						fieldWithPath(".createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					)
				)));
	}

	@Test
	void getFeedCommentsTest() throws Exception {

		Long feedId = 1L;
		String sort = "new";
		int page = 1;

		given(commentService.getFeedComments(anyLong(), anyString(), anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(commentMapper.getFeedCommentsToPageResponse(Mockito.any(), Mockito.any(LikeService.class)))
			.willReturn(GET_COMMENT_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/feeds/{feed_id}/comments?sort={sort}&page={page}", feedId, sort, page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Feed_Comments",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				),
				requestParameters(
					parameterWithName("sort").description("정렬 기준"),
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath(".data.[].commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
						fieldWithPath(".data.[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".data.[].userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath(".data.[].userInfo.id").type(JsonFieldType.NUMBER).description("회원 번호"),
						fieldWithPath(".data.[].userInfo.userId").type(JsonFieldType.STRING).description("회원 아이디"),
						fieldWithPath(".data.[].userInfo.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
						fieldWithPath(".data.[].userInfo.level").type(JsonFieldType.NUMBER).description("회원 레벨"),
						fieldWithPath(".data.[].userInfo.profileImage").type(JsonFieldType.STRING)
							.description("회원 프로필 사진"),
						fieldWithPath(".data.[].userInfo.thumbnailPath").type(JsonFieldType.STRING)
							.description("썸네일 주소"),
						fieldWithPath(".data.[].body").type(JsonFieldType.STRING).description("댓글 내용"),
						fieldWithPath(".data.[].isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
						fieldWithPath(".data.[].likeCount").type(JsonFieldType.NUMBER).description("댓글 좋아요"),
						fieldWithPath(".data.[].reportCount").type(JsonFieldType.NUMBER).description("댓글 신고 개수"),
						fieldWithPath(".data.[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".data.[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 댓글 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void deleteCommentTest() throws Exception {

		Long commentId = 1L;

		doNothing().when(commentService).deleteComment(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/comments/{comment_id}/delete", commentId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Comment",
					pathParameters(
						parameterWithName("comment_id").description("코멘트 번호")
					)
				)
			);
	}
}
