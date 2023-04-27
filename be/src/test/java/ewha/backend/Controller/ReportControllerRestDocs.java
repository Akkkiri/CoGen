package ewha.backend.Controller;

import static ewha.backend.Controller.constant.QuizControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ewha.backend.domain.report.service.ReportService;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ReportControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private ReportService reportService;

	@Test
	void feedReportTest() throws Exception {

		Long feedId = 1L;

		given(reportService.feedReport(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/feeds/{feed_id}/report", feedId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Feed_Report",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				)));
	}

	@Test
	void commentReportTest() throws Exception {

		Long commentId = 1L;

		given(reportService.commentReport(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/comments/{comment_id}/report", commentId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Comment_Report",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("comment_id").description("코멘트 번호")
				)));
	}

	@Test
	void answerReportTest() throws Exception {

		Long answerId = 1L;

		given(reportService.answerReport(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/answers/{answer_id}/report", answerId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Answer_Report",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("answer_id").description("코멘트 번호")
				)));
	}
}
