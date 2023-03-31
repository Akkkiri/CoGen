package ewha.backend.domain.feed.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.category.entity.Category;
import ewha.backend.domain.category.service.CategoryService;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.config.CustomPage;

@Mapper(componentModel = "spring")
public interface FeedMapper {

	default Feed feedPostToFeed(FeedDto.Post postFeed, CategoryService categoryService) {

		Category category = categoryService.findVerifiedCategory(postFeed.getCategory());

		return Feed.builder()
			.title(postFeed.getTitle())
			.category(category)
			.body(postFeed.getBody())
			.build();
	}

	default Feed feedPatchToFeed(FeedDto.Patch patchFeed, CategoryService categoryService) {

		Category category = categoryService.findVerifiedCategory(patchFeed.getCategory());

		return Feed.builder()
			.title(patchFeed.getTitle())
			.category(category)
			.body(patchFeed.getBody())
			.build();
	}

	default FeedDto.Response feedGetToFeedResponse(
		Feed feed, Boolean isLikedFeed, Boolean isMyFeed, Boolean isSavedFeed) {

		User findUser = feed.getUser();

		UserDto.BasicResponse basicResponse =
			UserDto.BasicResponse.builder()
				.id(findUser.getId())
				.userId(findUser.getUserId())
				.nickname(findUser.getNickname())
				.level(findUser.getLevel())
				.profileImage(findUser.getProfileImage())
				.thumbnailPath(findUser.getThumbnailPath())
				.build();

		return FeedDto.Response.builder()
			.feedId(feed.getId())
			.category(feed.getCategory().getCategoryType().toString())
			.userInfo(basicResponse)
			.title(feed.getTitle())
			.body(feed.getBody())
			.isLiked(isLikedFeed)
			.isMyFeed(isMyFeed)
			.isSavedFeed(isSavedFeed)
			.commentCount(feed.getCommentCount())
			.likeCount(feed.getLikeCount())
			.viewCount(feed.getViewCount())
			.imagePath(feed.getImagePath())
			.thumbnailPath(feed.getThumbnailPath())
			.reportCount(feed.getReportCount())
			.createdAt(feed.getCreatedAt())
			.modifiedAt(feed.getModifiedAt())
			.build();
	}

	default PageImpl<FeedDto.PageResponse> userFeedsToPageResponse(Page<Feed> feedPage) {

		if (feedPage == null)
			return null;

		return new PageImpl<>(feedPage.stream()
			.map(feed -> {
				return FeedDto.PageResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.category(feed.getCategory().getCategoryType().toString())
					.commentCount(feed.getComments().size())
					.likeCount(feed.getLikeCount())
					.createdAt(feed.getCreatedAt())
					.modifiedAt(feed.getModifiedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<FeedDto.PageResponse> myBookmarksToPageResponse(Page<Feed> feedPage) {

		if (feedPage == null) {
			return null;
		}

		return new PageImpl<>(feedPage.stream()
			.map(feed -> {
				return FeedDto.PageResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.category(feed.getCategory().getCategoryType().toString())
					.commentCount(feed.getComments().size())
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.modifiedAt(feed.getModifiedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<FeedDto.ListResponse> myFeedsToPageResponse(Page<Feed> feedList) {

		if (feedList == null)
			return null;

		return new PageImpl<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.category(feed.getCategory().getCategoryType().toString())
					.commentCount(feed.getCommentCount())
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.modifiedAt(feed.getModifiedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<FeedDto.ListResponse> feedsToPageResponse(Page<Feed> feedList) {

		if (feedList == null)
			return null;

		return new PageImpl<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.nickname(feed.getUser().getNickname())
					.title(feed.getTitle())
					.body(feed.getBody())
					.category(feed.getCategory().getCategoryType().toString())
					.commentCount(feed.getCommentCount())
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default CustomPage<FeedDto.ListResponse> newFeedsToCustomPageResponse(CustomPage<Feed> feedList) {

		if (feedList == null)
			return null;

		return new CustomPage<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.userId(feed.getUser().getUserId())
					.commentCount(feed.getCommentCount())
					.category(feed.getCategory().getCategoryType().toString())
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default List<FeedDto.BestResponse> feedListToBestResponseList(List<Feed> feedList) {

		if (feedList == null)
			return null;

		return feedList.stream()
			.map(feed -> {
				return FeedDto.BestResponse.builder()
					.feedId(feed.getId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.userId(feed.getUser().getUserId())
					.nickname(feed.getUser().getNickname())
					.profileImage(feed.getUser().getProfileImage())
					.thumbnailPath(feed.getUser().getThumbnailPath())
					.commentCount(feed.getCommentCount())
					.likeCount(feed.getLikeCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList());
	}

}
