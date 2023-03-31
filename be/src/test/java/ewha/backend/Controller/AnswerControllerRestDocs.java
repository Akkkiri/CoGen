package ewha.backend.Controller;

import static ewha.backend.Controller.constant.AnswerControllerConstant.*;
import static ewha.backend.Controller.constant.CommentControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.google.gson.Gson;

import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.mapper.AnswerMapper;
import ewha.backend.domain.question.service.AnswerService;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AnswerControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private AnswerMapper answerMapper;
	@MockBean
	private AnswerService answerService;

	@Test
	void postAnswerTest() throws Exception {

		Long questionId = 1L;

		String content = gson.toJson(POST_ANSWER_DTO);

		given(answerMapper.postAnswerToAnswer(Mockito.any(AnswerDto.Post.class)))
			.willReturn(Answer.builder().build());
		given(answerService.postAnswer(Mockito.any(Answer.class), anyLong()))
			.willReturn(Answer.builder().build());
		given(answerMapper.answerToAnswerResponse(Mockito.any(Answer.class))).willReturn(ANSWER_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/questions/{question_id}/answer/add", questionId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andDo(document(
				"Post_Answer",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("question_id").description("피드 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("answerBody").type(JsonFieldType.STRING).description("답변 내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("answerId").type(JsonFieldType.NUMBER).description("답변 번호"),
						fieldWithPath("answerBody").type(JsonFieldType.STRING).description("답변 내용")
					)
				)));
	}

	@Test
	void patchAnswerTest() throws Exception {

		Long answerId = 1L;

		String content = gson.toJson(PATCH_ANSWER_DTO);

		given(answerMapper.patchAnswerToAnswer(Mockito.any(AnswerDto.Patch.class)))
			.willReturn(Answer.builder().build());
		given(answerService.patchAnswer(Mockito.any(Answer.class), anyLong()))
			.willReturn(Answer.builder().build());
		given(answerMapper.answerToAnswerResponse(Mockito.any(Answer.class))).willReturn(ANSWER_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/answers/{answer_id}/edit", answerId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Patch_Answer",
				getDocumentRequest(),
				getDocumentResponse(),
				pathParameters(
					parameterWithName("answer_id").description("답변 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("answerBody").type(JsonFieldType.STRING).description("답변 내용")
					)
				),
				responseFields(
					List.of(
						fieldWithPath("answerId").type(JsonFieldType.NUMBER).description("답변 번호"),
						fieldWithPath("answerBody").type(JsonFieldType.STRING).description("답변 내용")
					)
				)));
	}

	@Test
	void getQuestionAnswersTest() throws Exception {

		Long questionId = 1L;
		String sort = "new";
		int page = 1;

		given(answerService.findQuestionAnswers(anyLong(), anyString(), anyInt()))
			.willReturn(new PageImpl<>(new ArrayList<>()));
		given(answerMapper.answerPageToListResponse(Mockito.any()))
			.willReturn(ANSWER_LIST_PAGE_RESPONSE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/questions/{question_id}/answer/list?sort={sort}&page={page}", questionId, sort, page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Question_Answers",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("question_id").description("질문 번호")
				),
				requestParameters(
					parameterWithName("sort").description("정렬 기준"),
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath(".data.[].answerId").type(JsonFieldType.NUMBER).description("답변 번호"),
						fieldWithPath(".data.[].userNickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
						fieldWithPath(".data.[].profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
						fieldWithPath(".data.[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 이미지"),
						fieldWithPath(".data.[].answerBody").type(JsonFieldType.STRING).description("답변 내용"),
						fieldWithPath(".data.[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
						fieldWithPath(".data.[].reportCount").type(JsonFieldType.NUMBER).description("신고 개수"),
						fieldWithPath(".data.[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".data.[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 답글 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void deleteAnswerTest() throws Exception {

		Long answerId = 1L;

		doNothing().when(answerService).deleteAnswer(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/answers/{answer_id}/delete", answerId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Answer",
					pathParameters(
						parameterWithName("answer_id").description("질문 번호")
					)
				)
			);
	}
}
