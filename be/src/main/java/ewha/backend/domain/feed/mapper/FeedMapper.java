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
			.imagePath(postFeed.getImagePath())
			.imagePath2(postFeed.getImagePath2())
			.imagePath3(postFeed.getImagePath3())
			.thumbnailPath(postFeed.getThumbnailPath())
			.build();
	}

	default Feed feedPatchToFeed(FeedDto.Patch patchFeed, CategoryService categoryService) {

		Category category = categoryService.findVerifiedCategory(patchFeed.getCategory());

		return Feed.builder()
			.title(patchFeed.getTitle())
			.category(category)
			.body(patchFeed.getBody())
			.imagePath(patchFeed.getImagePath())
			.imagePath2(patchFeed.getImagePath2())
			.imagePath3(patchFeed.getImagePath3())
			.thumbnailPath(patchFeed.getThumbnailPath())
			.build();
	}

	default FeedDto.Response feedGetToFeedResponse(
		Feed feed, Boolean isLikedFeed, Boolean isMyFeed, Boolean isSavedFeed) {

		User findUser = feed.getUser();

		String[] nick = findUser.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		UserDto.BasicResponse basicResponse =
			UserDto.BasicResponse.builder()
				.id(findUser.getId())
				.userId(findUser.getUserId())
				.nickname(nickPre)
				.hashcode(nickSuf)
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
			.imagePath2(feed.getImagePath2())
			.imagePath3(feed.getImagePath3())
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

				String[] nick = feed.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return FeedDto.PageResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.nickname(nickPre)
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

				String[] nick = feed.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return FeedDto.PageResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.nickname(nickPre)
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

				String[] nick = feed.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.nickname(nickPre)
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

				String[] nick = feed.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.userId(feed.getUser().getUserId())
					.nickname(nickPre)
					.hashcode(nickSuf)
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

				String[] nick = feed.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return FeedDto.BestResponse.builder()
					.feedId(feed.getId())
					.id(feed.getUser().getId())
					.userId(feed.getUser().getUserId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.nickname(nickPre)
					.hashcode(nickSuf)
					.profileImage(feed.getUser().getProfileImage())
					.thumbnailPath(feed.getUser().getThumbnailPath())
					.commentCount(feed.getCommentCount())
					.likeCount(feed.getLikeCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList());
	}

}
