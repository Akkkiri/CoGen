package ewha.backend.Controller.constant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.category.dto.CategoryDto;
import ewha.backend.domain.category.entity.CategoryType;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.global.config.CustomPage;

public class FeedControllerConstant {

	public static final UserDto.BasicResponse USER_BASIC_RESPONSE =
		UserDto.BasicResponse.builder()
			.id(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.level(1)
			.profileImage("프로필 이미지")
			.thumbnailPath("썸네일 이미지")
			.build();

	public static final  CategoryDto.Response CATEGORY_DTO =
		CategoryDto.Response.builder()
			.categoryType(CategoryType.PLACE)
			.build();

	public static final FeedDto.Post POST_FEED_DTO =
		FeedDto.Post.builder()
			.title("피드 제목")
			.body("피드 내용")
			.category(CategoryType.PLACE)
			.build();

	public static final FeedDto.Response POST_FEED_RESPONSE_DTO =
		FeedDto.Response.builder()
			.feedId(1L)
			.category(CategoryType.PLACE.toString())
			.userInfo(USER_BASIC_RESPONSE)
			.title("피드 제목")
			.body("피드 내용")
			.isLiked(false)
			.isMyFeed(true)
			.isSavedFeed(false)
			.commentCount(0L)
			.likeCount(0L)
			.viewCount(1L)
			.reportCount(0L)
			.imagePath("이미지 주소")
			.thumbnailPath("썸네일 주소")
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final FeedDto.Patch PATCH_FEED_DTO =
		FeedDto.Patch.builder()
			.title("피드 제목")
			.body("피드 내용")
			.category(CategoryType.PLACE)
			.imagePath("이미지 주소")
			.build();

	public static final FeedDto.Response GET_FEED_RESPONSE_DTO =
		FeedDto.Response.builder()
			.feedId(1L)
			.category(CategoryType.PLACE.toString())
			.userInfo(USER_BASIC_RESPONSE)
			.title("피드 제목")
			.body("피드 내용")
			.isLiked(true)
			.isMyFeed(false)
			.isSavedFeed(false)
			.commentCount(0L)
			.likeCount(0L)
			.viewCount(1L)
			.reportCount(0L)
			.imagePath("이미지 주소")
			.thumbnailPath("썸네일 주소")
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();
	public static final FeedDto.ListResponse FEED_LIST_RESPONSE =
		FeedDto.ListResponse.builder()
			.feedId(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.category(CategoryType.PLACE.toString())
			.title("피드 제목")
			.body("피드 내용")
			.commentCount(1L)
			.likeCount(1L)
			.viewCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final CustomPage<FeedDto.ListResponse> FEED_LIST_RESPONSE_PAGE =
		new CustomPage<>(List.of(FEED_LIST_RESPONSE, FEED_LIST_RESPONSE));

	public static final PageImpl<FeedDto.ListResponse> FEED_SEARCH_RESPONSE_PAGE =
		new PageImpl<>(List.of(FEED_LIST_RESPONSE, FEED_LIST_RESPONSE));

	public static final FeedDto.BestResponse BEST_FEED_RESPONSE =
		FeedDto.BestResponse.builder()
			.feedId(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.profileImage("사용자 프로필 이미지")
			.thumbnailPath("썸네일 경로")
			.title("피드 제목")
			.body("피드 내용")
			.commentCount(1L)
			.likeCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final List<FeedDto.BestResponse> BEST_FEED_RESPONSE_LIST =
		new ArrayList<>(
			List.of(
				BEST_FEED_RESPONSE, BEST_FEED_RESPONSE
			)
		);
}
