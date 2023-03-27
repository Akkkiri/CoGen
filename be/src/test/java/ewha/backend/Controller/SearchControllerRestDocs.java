// package com.ewha.back.Controller;
//
// import static com.ewha.back.Controller.constant.FeedControllerConstant.*;
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.*;
// import static org.mockito.BDDMockito.*;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.restdocs.request.RequestDocumentation.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.http.MediaType;
// import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
// import org.springframework.restdocs.payload.JsonFieldType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.ewha.back.domain.feed.mapper.FeedMapper;
// import com.ewha.back.domain.search.service.SearchService;
// import com.google.gson.Gson;
//
// @Transactional
// @SpringBootTest
// @AutoConfigureMockMvc
// @AutoConfigureRestDocs
// public class SearchControllerRestDocs {
//
// 	@Autowired
// 	private MockMvc mockMvc;
// 	@Autowired
// 	private Gson gson;
// 	@MockBean
// 	private SearchService searchService;
// 	@MockBean
// 	private FeedMapper feedMapper;
//
// 	@Test
// 	void getSearchResultTest() throws Exception {
//
// 		String category = "CULTURE";
// 		String query = "검색어";
// 		Integer page = 1;
//
// 		given(searchService.findCategoryFeedsPageByQueryParam(anyString(), anyString(), anyInt()))
// 			.willReturn(new PageImpl<>(new ArrayList<>()));
// 		given(feedMapper.newFeedsToPageResponse(Mockito.any(Page.class))).willReturn(FEED_SEARCH_RESPONSE_PAGE);
//
// 		ResultActions actions =
// 			mockMvc.perform(
// 				RestDocumentationRequestBuilders
// 					.get("/search?category={category}&query={query}&page={page}", category, query, page)
// 					.accept(MediaType.APPLICATION_JSON)
// 			);
//
// 		actions
// 			.andExpect(status().isOk())
// 			.andDo(document(
// 				"Get_Search_Result",
// 				getDocumentRequest(),
// 				getDocumentResponse(),
// 				requestParameters(
// 					parameterWithName("category").description("카테고리"),
// 					parameterWithName("query").description("검색어"),
// 					parameterWithName("page").description("페이지 번호")
// 				),
// 				responseFields(
// 					List.of(
// 						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
// 						fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
// 						fieldWithPath("data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
// 						fieldWithPath("data[].categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
// 						fieldWithPath("data[].title").type(JsonFieldType.STRING).description("피드 제목"),
// 						fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
// 						fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
// 						fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
// 						fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
// 						fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
// 						fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
// 						fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
// 						fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
// 						fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
// 						fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
// 					)
// 				)));
// 	}
// }
