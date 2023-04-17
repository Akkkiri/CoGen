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

import com.google.gson.Gson;

import ewha.backend.domain.bookmark.service.BookmarkService;
import ewha.backend.domain.user.service.UserService;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BookmarkControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private UserService userService;
	@MockBean
	private BookmarkService bookmarkService;

	@Test
	void saveFeedTest() throws Exception {

		Long feedId = 1L;

		given(bookmarkService.saveFeed(anyLong())).willReturn("");

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/feeds/{feed_id}/save", feedId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Bookmark_Feed",
				getDocumentRequest(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				)
			));
	}
}
