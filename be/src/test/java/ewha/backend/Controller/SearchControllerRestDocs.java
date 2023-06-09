package ewha.backend.Controller;

import static ewha.backend.Controller.constant.FeedControllerConstant.*;
import static ewha.backend.Controller.constant.UserControllerConstant.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.search.service.SearchService;
import ewha.backend.domain.user.mapper.UserMapper;

import com.google.gson.Gson;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SearchControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private SearchService searchService;
	@MockBean
	private FeedMapper feedMapper;
	@MockBean
	private UserMapper userMapper;

	@Test
	void getSearchResultTest() throws Exception {

		String category = "CULTURE";
		String query = "검색어";
		String sort = "정렬방식";
		Integer page = 1;

		given(searchService.findCategoryFeedsPageByQueryParam(anyString(), anyString(), anyString(), anyInt()))
			.willReturn(new PageImpl<>(new ArrayList<>()));
		given(feedMapper.feedsToPageResponse(Mockito.any(Page.class))).willReturn(FEED_SEARCH_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/search?category={category}&query={query}&sort={sort}&page={page}",
						category, sort, query, page)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Search_Result",
				getDocumentRequest(),
				getDocumentResponse(),
				requestParameters(
					parameterWithName("category").description("카테고리"),
					parameterWithName("query").description("검색어"),
					parameterWithName("sort").description("정렬 기준"),
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath("data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
						fieldWithPath("data[].hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath("data[].category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath("data[].title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath("data[].body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
						fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
						fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
						fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}

	@Test
	void getUserSearchResultTest() throws Exception {

		String query = "검색어";
		Integer page = 1;

		given(searchService.findUserPageByQuery(anyString(), anyInt()))
			.willReturn(new PageImpl<>(new ArrayList<>()));
		given(userMapper.userPageToUserSearchResponse(Mockito.any(Page.class))).willReturn(USER_SEARCH_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/search/user?query={query}&page={page}", query, page)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_User_Search_Result",
				getDocumentRequest(),
				getDocumentResponse(),
				requestParameters(
					parameterWithName("query").description("검색어"),
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("작성자 아이디"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
						fieldWithPath("data[].hashcode").type(JsonFieldType.STRING).description("사용자 해시코드"),
						fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("프로피 이미지"),
						fieldWithPath("data[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 경로"),
						fieldWithPath("data[].isFollowing").type(JsonFieldType.BOOLEAN).description("친구신청 여부"),
						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 사용자 수"),
						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
					)
				)));
	}
}
