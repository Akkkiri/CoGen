package ewha.backend.Controller;

import static ewha.backend.Controller.constant.FeedControllerConstant.*;
import static ewha.backend.Controller.utils.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
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

import ewha.backend.Controller.utils.WithMockCustomUser;
import ewha.backend.domain.category.service.CategoryService;
import ewha.backend.domain.comment.service.CommentService;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.security.jwtTokenizer.JwtTokenizer;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class FeedControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private UserMapper userMapper;
	@MockBean
	private FeedMapper feedMapper;
	@MockBean
	private FeedService feedService;
	@MockBean
	private UserService userService;
	@MockBean
	private LikeService likeService;
	@MockBean
	private CommentService commentService;
	@MockBean
	private JwtTokenizer jwtTokenizer;
	@MockBean
	private AwsS3Service awsS3Service;

	@Test
	void postFeedTest() throws Exception {

		String content = gson.toJson(POST_FEED_DTO);

		MockMultipartFile json =
			new MockMultipartFile("post", "dto",
				"application/json", content.getBytes(StandardCharsets.UTF_8));

		MockMultipartFile image =
			new MockMultipartFile("image", "image.png",
				"image/png", "<<png data>>".getBytes());

		given(feedMapper.feedPostToFeed(Mockito.any(FeedDto.Post.class), Mockito.any(CategoryService.class)))
			.willReturn(Feed.builder().build());
		given(feedService.createFeed(Mockito.any(Feed.class))).willReturn(Feed.builder().build());
		given(awsS3Service.uploadImageToS3(Mockito.any(MultipartFile.class), anyLong())).willReturn(new ArrayList<>());
		// given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/api/feeds/add")
					.file(json)
					.file(image)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.accept(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isCreated())
			.andDo(document(
				"Post_Feed",
				getDocumentRequest(),
				// getDocumentResponse(),
				requestFields(
					List.of(
						fieldWithPath("title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath("category").type(JsonFieldType.STRING).description("피드 카테고리")
					)
					// ),
					// responseFields(
					// 	List.of(
					// 		fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
					// 		fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
					// 		fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
					// 		fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
					// 		fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
					// 		fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
					// 		fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
					// 		fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
					// 			.description("작성자 프로필 사진"),
					// 		fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
					// 		fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
					// 		fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
					// 		fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
					// 		fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
					// 		fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
					// 		fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
					// 		fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
					// 		fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					// 	)
				)));

	}

	@Test
	void patchFeedTest() throws Exception {

		Long feedId = 1L;

		String content = gson.toJson(PATCH_FEED_DTO);

		MockMultipartFile json =
			new MockMultipartFile("patch", "dto",
				"application/json", content.getBytes(StandardCharsets.UTF_8));

		MockMultipartFile image =
			new MockMultipartFile("image", "image.png",
				"image/png", "<<png data>>".getBytes());

		given(feedService.findVerifiedFeed(anyLong())).willReturn(Feed.builder().build());
		given(feedMapper.feedPatchToFeed(Mockito.any(FeedDto.Patch.class), Mockito.any(CategoryService.class)))
			.willReturn(Feed.builder().build());
		given(feedService.updateFeed(Mockito.any(Feed.class), anyLong())).willReturn(Feed.builder().build());
		// given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(POST_FEED_RESPONSE_DTO);
		doNothing().when(feedService).saveFeed(Mockito.any(Feed.class));

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/api/feeds/{feed_id}/edit", feedId)
					.file(json)
					.file(image)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.accept(MediaType.APPLICATION_JSON)
					.content(content)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Patch_Feed",
				getDocumentRequest(),
				// getDocumentResponse(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				),
				requestFields(
					List.of(
						fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 제목"),
						fieldWithPath("body").type(JsonFieldType.STRING).description("수정된 내용"),
						fieldWithPath("category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지 주소")
					)
					// ),
					// responseFields(
					// 	List.of(
					// 		fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
					// 		fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
					// 		fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
					// 		fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
					// 		fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
					// 		fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
					// 		fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
					// 		fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING)
					// 			.description("작성자 프로필 사진"),
					// 		fieldWithPath("data.title").type(JsonFieldType.STRING).description("수정된 제목"),
					// 		fieldWithPath("data.body").type(JsonFieldType.STRING).description("수정된 내용"),
					// 		fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
					// 		fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
					// 		fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
					// 		fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
					// 		fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
					// 		fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
					// 		fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					// 	)
				)));
	}

	@Test
	@WithMockCustomUser
	void getFeedTest() throws Exception {

		Long feedId = 1L;

		given(feedService.updateView(anyLong())).willReturn(Feed.builder().build());
		given(likeService.isLikedFeed(Mockito.any(Feed.class))).willReturn(Boolean.FALSE);
		given(feedService.isMyFeed(Mockito.any(Feed.class))).willReturn(Boolean.FALSE);
		given(feedService.isSavedFeed(Mockito.any(Feed.class))).willReturn(Boolean.FALSE);
		given(feedMapper.feedGetToFeedResponse(Mockito.any(Feed.class), anyBoolean(), anyBoolean(), anyBoolean()))
			.willReturn(GET_FEED_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/feeds/{feed_id}", feedId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value(GET_FEED_RESPONSE_DTO.getTitle()))
			.andDo(document(
				"Get_Feed",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("feed_id").description("피드 번호")
				),
				responseFields(
					List.of(
						fieldWithPath(".feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath(".category").type(JsonFieldType.STRING).description("피드 카테고리"),
						fieldWithPath(".userInfo.id").type(JsonFieldType.NUMBER).description("작성자 번호"),
						fieldWithPath(".userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath(".userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
						fieldWithPath(".userInfo.level").type(JsonFieldType.NUMBER).description("작성자 레벨"),
						fieldWithPath(".userInfo.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
						fieldWithPath(".userInfo.thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath(".body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath(".isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
						fieldWithPath(".isMyFeed").type(JsonFieldType.BOOLEAN).description("내 피드 여부"),
						fieldWithPath(".isSavedFeed").type(JsonFieldType.BOOLEAN).description("북마크 저장 여부"),
						fieldWithPath(".commentCount").type(JsonFieldType.NUMBER).description("피드 댓글 개수"),
						fieldWithPath(".likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
						fieldWithPath(".viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
						fieldWithPath(".reportCount").type(JsonFieldType.NUMBER).description("피드 신고 개수"),
						fieldWithPath(".imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
						fieldWithPath(".thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath(".createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath(".modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					)
				)));
	}

	@Test
	void getWeeklyBestFeedsTest() throws Exception {

		given(feedService.findWeeklyBestFeed())
			.willReturn(new ArrayList<>());
		given(feedMapper.feedListToBestResponseList(anyList())).willReturn(BEST_FEED_RESPONSE_LIST);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/feeds/weekly")
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Weekly_Best_Feeds",
				getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath("[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath("[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
						fieldWithPath("[].profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
						fieldWithPath("[].thumbnailPath").type(JsonFieldType.STRING).description("썸네일 주소"),
						fieldWithPath("[].title").type(JsonFieldType.STRING).description("피드 제목"),
						fieldWithPath("[].body").type(JsonFieldType.STRING).description("피드 내용"),
						fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
						fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
						fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
						fieldWithPath("[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
					)
				)));
	}

	@Test
	void getCategoryFeedsTest() throws Exception {

		String categoryName = "PLACE";
		String sort = "likes";
		int page = 1;

		given(feedService.findCategoryFeeds(anyString(), anyString(), anyInt()))
			.willReturn(new PageImpl<>(new ArrayList<>()));
		given(feedMapper.feedsToPageResponse(Mockito.any())).willReturn(FEED_LIST_RESPONSE_PAGE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders
					.get("/api/feeds/categories?category={categoryName}&sort={sort}&page={page}", categoryName, sort, page)
					.accept(MediaType.APPLICATION_JSON)
			);
		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Get_Category_Feeds",
				getDocumentResponse(),
				requestParameters(
					parameterWithName("category").description("피드 카테고리"),
					parameterWithName("sort").description("정렬 기준"),
					parameterWithName("page").description("페이지 번호")
				),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
						fieldWithPath("data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
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

	//
	// @Test
	// void getNewestFeedsTest() throws Exception {
	//
	// 	int page = 1;
	//
	// 	given(feedService.findNewestFeeds(anyInt())).willReturn(new CustomPage<>(new ArrayList<>()));
	// 	given(feedMapper.TESTnewFeedsToPageResponse(Mockito.any())).willReturn(FEED_LIST_RESPONSE_PAGE);
	//
	// 	ResultActions actions =
	// 		mockMvc.perform(
	// 			RestDocumentationRequestBuilders.get("/feeds/newest?page={page}", page)
	// 				.accept(MediaType.APPLICATION_JSON)
	// 		);
	//
	// 	actions
	// 		.andExpect(status().isOk())
	// 		.andDo(document(
	// 			"Get_Newest_Feeds",
	// 			getDocumentResponse(),
	// 			requestParameters(
	// 				parameterWithName("page").description("페이지 번호")
	// 			),
	// 			responseFields(
	// 				List.of(
	// 					fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
	// 					fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
	// 					fieldWithPath("data[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
	// 					fieldWithPath("data[].categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
	// 					fieldWithPath("data[].title").type(JsonFieldType.STRING).description("피드 제목"),
	// 					fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
	// 					fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
	// 					fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
	// 					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
	// 					fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜"),
	// 					fieldWithPath(".pageInfo").type(JsonFieldType.OBJECT).description("Pageble 설정"),
	// 					fieldWithPath(".pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
	// 					fieldWithPath(".pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
	// 					fieldWithPath(".pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레이팅 수"),
	// 					fieldWithPath(".pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
	// 				)
	// 			)));
	//
	// }

	@Test
	void deleteFeedTest() throws Exception {

		Long feedId = 1L;

		doNothing().when(feedService).deleteFeed(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/feeds/{feed_id}/delete", feedId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Feed",
					pathParameters(
						parameterWithName("feed_id").description("피드 번호")
					)
				)
			);
	}
}
