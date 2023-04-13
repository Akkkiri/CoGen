package ewha.backend.domain.feed.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ewha.backend.domain.category.service.CategoryService;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.global.config.CustomPage;
import ewha.backend.global.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {
	private final FeedMapper feedMapper;
	private final FeedService feedService;
	private final CategoryService categoryService;
	private final LikeService likeService;
	private final AwsS3Service awsS3Service;

	@PostMapping("/add")
	public ResponseEntity<Long> postFeed(
		@Valid @RequestBody FeedDto.Post postFeed) throws Exception {

		Feed feed = feedMapper.feedPostToFeed(postFeed, categoryService);
		Feed createdFeed = feedService.createFeed(feed);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdFeed.getId());
	}

	// @PostMapping("/add")
	// public ResponseEntity<HttpStatus> postFeed(
	// 	@RequestParam(value = "images", required = false) MultipartFile multipartFile,
	// 	@Valid @RequestPart(value = "post") FeedDto.Post postFeed) throws Exception {
	//
	// 	List<String> imagePath = null;
	//
	// 	Feed feed = feedMapper.feedPostToFeed(postFeed, categoryService);
	// 	Feed createdFeed = feedService.createFeed(feed);
	//
	// 	if (multipartFile != null) {
	// 		imagePath = awsS3Service.uploadImageToS3(multipartFile, createdFeed.getId());
	// 		if (imagePath.size() != 0) {
	// 			createdFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
	// 		}
	// 	}
	//
	// 	// FeedDto.Response response = feedMapper.feedToFeedResponse(createdFeed);
	// 	// return ResponseEntity.status(HttpStatus.CREATED).body(response);
	// 	return ResponseEntity.status(HttpStatus.CREATED).build();
	// }

	@PatchMapping("/{feed_id}/edit")
	public ResponseEntity<Long> patchFeed(@PathVariable("feed_id") @Positive Long feedId,
		@Valid @RequestBody FeedDto.Patch patchFeed) throws Exception {

		Feed findFeed = feedService.findVerifiedFeed(feedId);
		Feed feed = feedMapper.feedPatchToFeed(patchFeed, categoryService);
		Feed updatedFeed = feedService.updateFeed(feed, feedId);

		return ResponseEntity.status(HttpStatus.OK).body(feedId);
	}

	// @PostMapping("/{feed_id}/edit")
	// public ResponseEntity<HttpStatus> patchFeed(@PathVariable("feed_id") @Positive Long feedId,
	// 	@RequestParam(value = "image", required = false) MultipartFile multipartFile,
	// 	@Valid @RequestPart(value = "patch") FeedDto.Patch patchFeed) throws Exception {
	//
	// 	List<String> imagePath = null;
	//
	// 	Feed findFeed = feedService.findVerifiedFeed(feedId);
	// 	Feed feed = feedMapper.feedPatchToFeed(patchFeed, categoryService);
	// 	Feed updatedFeed = feedService.updateFeed(feed, feedId);
	//
	// 	// MultipartFile이 없으면서, 기존 피드에 이미지가 있고, 요청 JSON에도 이미지가 있고, 두 경로가 같은 경우
	// 	if (multipartFile == null && findFeed.getImagePath() != null && patchFeed.getImagePath() != null
	// 		&& patchFeed.getImagePath().equals(updatedFeed.getImagePath())) {
	// 		updatedFeed.addImagePaths(updatedFeed.getImagePath(), updatedFeed.getThumbnailPath());
	// 		// 기존 피드에 이미지가 있고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
	// 	} else if (findFeed.getImagePath() != null && patchFeed.getImagePath() == null && multipartFile != null) {
	// 		imagePath = awsS3Service.updateORDeleteFeedImageFromS3(updatedFeed.getId(), multipartFile);
	// 		updatedFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
	// 		// 기존 피드에 이미지가 없고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
	// 	} else if (findFeed.getImagePath() == null && patchFeed.getImagePath() == null && multipartFile != null) {
	// 		imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedFeed.getId());
	// 		updatedFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
	// 		// 기존 피드에 이미지가 있으면서 요청 JSON에 이미지가 없고, multipartFile도 없는 경우
	// 	} else if (findFeed.getImagePath() != null && patchFeed.getImagePath() == null && multipartFile == null) {
	// 		awsS3Service.updateORDeleteFeedImageFromS3(updatedFeed.getId(), multipartFile);
	// 		updatedFeed.addImagePaths(null, null);
	// 	}
	//
	// 	feedService.saveFeed(updatedFeed);
	//
	// 	// FeedDto.Response response = feedMapper.feedToFeedResponse(updatedFeed);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).build();
	// }

	@GetMapping("/{feed_id}")
	public ResponseEntity<FeedDto.Response> getFeed(@PathVariable("feed_id") @Positive Long feedId) {

		Feed feed = feedService.updateView(feedId);
		Boolean isLikedFeed = likeService.isLikedFeed(feed);
		Boolean isMyFeed = feedService.isMyFeed(feed);
		Boolean isSavedFeed = feedService.isSavedFeed(feed);

		FeedDto.Response response = feedMapper.feedGetToFeedResponse(feed, isLikedFeed, isMyFeed, isSavedFeed);

		return ResponseEntity.status(HttpStatus.OK).body(response);

		// FeedDto.Response response;
		//
		// User findUser = userService.getLoginUserReturnNull();
		//
		// if (findUser != null) {
		//
		// 	Feed feed = feedService.updateView(feedId);
		// 	Boolean isLikedFeed = likeService.isLikedFeed(feed);
		// 	// List<CommentLike> isLikedComments = feedService.isLikedComments(feedId);
		// 	Boolean isMyFeed = feedService.isMyFeed(feed);
		// 	// List<Comment> myComments = commentService.isMyComments(feedId);
		//
		// 	// response = feedMapper.feedGetToFeedResponse(feed, isLikedFeed, isLikedComments, isMyFeed, myComments);
		// 	response = feedMapper.feedGetToFeedResponse(feed, isLikedFeed, isMyFeed);
		//
		// 	return ResponseEntity.status(HttpStatus.OK).body(response);
		// }
		//
		// // 비로그인 사용자
		// Feed feed = feedService.updateView(feedId);
		//
		// response = feedMapper.feedGetWithoutLoginToFeedResponse(feed);
		//
		// return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/newest")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getFeeds(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		CustomPage<Feed> feedList = feedService.findNewestFeeds(page);
		CustomPage<FeedDto.ListResponse> responses = feedMapper.newFeedsToCustomPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
		// return ResponseEntity.status(HttpStatus.OK).body(feedList);
	}

	@GetMapping("/categories")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getCategoryFeeds(
		@RequestParam(name = "category", defaultValue = "ALL") String categoryName,
		@RequestParam(name = "sort", defaultValue = "new") String sort,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = feedService.findCategoryFeeds(categoryName, sort, page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.feedsToPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
	}

	@GetMapping("/weekly")
	public ResponseEntity<List<FeedDto.BestResponse>> getWeeklyBestFeeds() {

		List<Feed> feedList = feedService.findWeeklyBestFeed();
		List<FeedDto.BestResponse> responses = feedMapper.feedListToBestResponseList(feedList);

		return ResponseEntity.ok().body(responses);
	}

	@DeleteMapping("/{feed_id}/delete")
	public ResponseEntity<String> deleteFeed(
		@PathVariable("feed_id") @Positive Long feedId) {

		feedService.deleteFeed(feedId);

		return ResponseEntity.noContent().build();
	}
}
