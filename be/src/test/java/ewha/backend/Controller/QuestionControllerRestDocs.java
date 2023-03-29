package ewha.backend.Controller;

import static ewha.backend.Controller.constant.QuestionControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.mapper.QuestionMapper;
import ewha.backend.domain.question.service.QuestionService;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class QuestionControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private QuestionService questionService;
	@MockBean
	private QuestionMapper questionMapper;
	@MockBean
	private AwsS3Service awsS3Service;

	@Test
	void postQuestionTest() throws Exception {

		String content = gson.toJson(POST_QUESTION_DTO);

		MockMultipartFile json =
			new MockMultipartFile("post", "dto",
				"application/json", content.getBytes(StandardCharsets.UTF_8));

		MockMultipartFile image =
			new MockMultipartFile("image", "image.png",
				"image/png", "<<png data>>".getBytes());

		given(questionMapper.questionPostToQuestion(Mockito.any(QuestionDto.Post.class)))
			.willReturn(Question.builder().build());
		given(questionService.createQuestion(Mockito.any(Question.class)))
			.willReturn(Question.builder().build());
		given(awsS3Service.uploadImageToS3(Mockito.any(MultipartFile.class), anyLong()))
			.willReturn(new ArrayList<>());
		given(questionMapper.questionToQuestionResponse(Mockito.any(Question.class)))
			.willReturn(QUESTION_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/api/questions/add")
					.file(json)
					.file(image)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.accept(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andDo(document(
				"Post_Question",
				getDocumentRequest(),
				requestFields(
					List.of(
						fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용")
					)
				)));
	}

	@Test
	void patchQuestionTest() throws Exception {

		Long questionId = 1L;

		String content = gson.toJson(PATCH_QUESTION_DTO);

		MockMultipartFile json =
			new MockMultipartFile("patch", "dto",
				"application/json", content.getBytes(StandardCharsets.UTF_8));

		MockMultipartFile image =
			new MockMultipartFile("image", "image.png",
				"image/png", "<<png data>>".getBytes());

		given(questionService.findVerifiedQuestion(anyLong()))
			.willReturn(Question.builder().build());
		given(questionMapper.questionPatchToQuestion(Mockito.any(QuestionDto.Patch.class)))
			.willReturn(Question.builder().build());
		given(questionService.updateQuestion(Mockito.any(Question.class), anyLong()))
			.willReturn(Question.builder().build());
		doNothing().when(questionService).saveQuestion(Mockito.any(Question.class));

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/api/questions/{question_id}/edit", questionId)
					.file(json)
					.file(image)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.accept(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Patch_Question",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("question_id").description("질문 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용"),
						fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
						fieldWithPath("thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소")
					)
				)));
	}

	@Test
	void getQuestionTest() throws Exception {

		given(questionService.getQuestion()).willReturn(Question.builder().build());
		given(questionMapper.questionToQuestionResponse(Mockito.any(Question.class))).willReturn(QUESTION_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/questions/weekly")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Question",
				getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
						fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용"),
						fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
						fieldWithPath("thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소")
					)
				)));
	}

	@Test
	void getPassedQuestionTest() throws Exception {

		int page = 1;

		given(questionService.getPassedQuestion(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
		given(questionMapper.questionsToPageResponse(Mockito.any(Page.class))).willReturn(QUESTION_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/questions/list?page={page}", page)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Question_List",
				getDocumentResponse(),requestParameters(
					parameterWithName("page").description("페이지 번호")
				),

				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
						fieldWithPath("data[].content").type(JsonFieldType.STRING).description("질문 내용"),
						fieldWithPath("data[].imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
						fieldWithPath("data[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 질문 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void deleteQuestionTest() throws Exception {

		Long questionId = 1L;

		doNothing().when(questionService).deleteQuestion(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/questions/{question_id}/delete", questionId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Question",
					pathParameters(
						parameterWithName("question_id").description("질문 번호")
					)
				)
			);
	}
}
