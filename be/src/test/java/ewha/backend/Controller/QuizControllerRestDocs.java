package ewha.backend.Controller;

import static ewha.backend.Controller.constant.QuizControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import ewha.backend.domain.quiz.dto.QuizDto;
import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.domain.quiz.mapper.QuizMapper;
import ewha.backend.domain.quiz.service.QuizService;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class QuizControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private QuizMapper quizMapper;
	@MockBean
	private QuizService quizService;

	@Test
	void postQuizTest() throws Exception {

		String content = gson.toJson(POST_QUIZ_DTO);

		given(quizMapper.quizPostToQuiz(Mockito.any(QuizDto.Post.class))).willReturn(Quiz.builder().build());
		given(quizService.updateQuiz(anyLong(), Mockito.any(Quiz.class))).willReturn(Quiz.builder().build());
		// given(quizMapper.quizToQuizResponse(Mockito.any(Quiz.class))).willReturn(QUIZ_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/quizzes/add")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andDo(document(
				"Post_Quiz",
				getDocumentRequest(),
				requestFields(
					List.of(
						fieldWithPath("content").type(JsonFieldType.STRING).description("퀴즈 내용"),
						fieldWithPath("explanation").type(JsonFieldType.STRING).description("퀴즈 설명"),
						fieldWithPath("answer").type(JsonFieldType.STRING).description("정답"),
						fieldWithPath("dummy1").type(JsonFieldType.STRING).description("더미 1"),
						fieldWithPath("dummy2").type(JsonFieldType.STRING).description("더미 2")
					)
				)));
	}

	@Test
	void patchQuizTest() throws Exception {

		Long quizId = 1L;

		String content = gson.toJson(PATCH_QUIZ_DTO);

		given(quizMapper.quizPatchToQuiz(Mockito.any(QuizDto.Patch.class))).willReturn(Quiz.builder().build());
		given(quizService.createQuiz(Mockito.any(Quiz.class))).willReturn(Quiz.builder().build());
		// given(quizMapper.quizToQuizResponse(Mockito.any(Quiz.class))).willReturn(QUIZ_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/quizzes/{quiz_id}/edit", quizId)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Patch_Quiz",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("quiz_id").description("퀴즈 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("content").type(JsonFieldType.STRING).description("퀴즈 내용"),
						fieldWithPath("explanation").type(JsonFieldType.STRING).description("퀴즈 설명"),
						fieldWithPath("answer").type(JsonFieldType.STRING).description("정답"),
						fieldWithPath("dummy1").type(JsonFieldType.STRING).description("더미 1"),
						fieldWithPath("dummy2").type(JsonFieldType.STRING).description("더미 2")
					)
				)));
	}

	@Test
	void getWeeklyQuizTest() throws Exception {

		given(quizService.findWeeklyQuizzes()).willReturn(new ArrayList<>());
		given(quizMapper.quizToQuizListResponse(anyList())).willReturn(QUIZ_RESPONSE_LIST);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/quizzes/weekly")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Weekly_Quiz_List",
				getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath("[].content").type(JsonFieldType.STRING).description("퀴즈 내용"),
						fieldWithPath("[].explanation").type(JsonFieldType.STRING).description("퀴즈 설명"),
						fieldWithPath("[].bodies[]").type(JsonFieldType.ARRAY).description("퀴즈 답변"),
						fieldWithPath("[].answer").type(JsonFieldType.STRING).description("정답")
					)
				)));
	}

	@Test
	void deleteQuizTest() throws Exception {

		Long quizId = 1L;

		doNothing().when(quizService).deleteQuiz(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/quizzes/{quiz_id}/delete", quizId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Quiz",
					pathParameters(
						parameterWithName("quiz_id").description("퀴즈 번호")
					)
				)
			);
	}
}
