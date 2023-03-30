package ewha.backend.domain.feed.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ewha.backend.domain.bookmark.repository.BookmarkRepository;
import ewha.backend.domain.comment.repository.CommentRepository;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedQueryRepository;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.domain.like.repository.CommentLikeQueryRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.config.CustomPage;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

	private final UserService userService;
	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final CommentRepository commentRepository;
	private final CommentLikeQueryRepository commentLikeQueryRepository;
	private final BookmarkRepository bookmarkRepository;
	private final AwsS3Service awsS3Service;

	@Override
	@Transactional
	public Feed createFeed(Feed feed) {

		User findUser = userService.getLoginUser();

		Feed savedFeed = Feed.builder()
			.user(findUser)
			.title(feed.getTitle())
			.body(feed.getBody())
			.category(feed.getCategory())
			.viewCount(0L)
			.likeCount(0L)
			.build();

		if (findUser.getDailyFeedCount() <= 5) {
			findUser.addAriFactor(2);
			findUser.addDailyFeedCount();

			if (findUser.getAriFactor() == 50) {
				findUser.addLevel();
			}
			userRepository.save(findUser);
		}

		return feedRepository.save(savedFeed);
	}

	@Override
	@Transactional
	public Feed updateFeed(Feed feed, Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {

			findFeed.updateFeed(feed);

			return feedRepository.save(findFeed);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	public Feed updateFeedImage(
		Long feedId, FeedDto.Patch patchFeedDto, Feed originalFeed,
		Feed updatedFeed, MultipartFile multipartFile) throws Exception {

		if (originalFeed.getImagePath() == null) {
			if (multipartFile == null) {
				if (patchFeedDto.getImagePath() != null) {
					throw new BusinessLogicException(ExceptionCode.IMAGE_PATH_NOT_MATCH);
				} else {
					return updatedFeed;
				}
			} else {
				List<String> newImagePaths = awsS3Service.uploadImageToS3(multipartFile, feedId);
				updatedFeed.addImagePaths(newImagePaths.get(0), newImagePaths.get(1));
				return updatedFeed;
			}
		} else {
			if (multipartFile == null) {
				if (patchFeedDto.getImagePath() != null
					&& !patchFeedDto.getImagePath().equals(originalFeed.getImagePath())) {
					throw new BusinessLogicException(ExceptionCode.IMAGE_PATH_NOT_MATCH);
				} else if (patchFeedDto.getImagePath() != null
					&& patchFeedDto.getImagePath().equals(originalFeed.getImagePath())) {
					updatedFeed.addImagePaths(originalFeed.getImagePath(), originalFeed.getThumbnailPath());
					return updatedFeed;
				} else {
					awsS3Service.deleteImageFromS3(originalFeed.getImagePath());
					awsS3Service.deleteImageFromS3(originalFeed.getThumbnailPath());
					updatedFeed.addImagePaths(null, null);
					return updatedFeed;
				}
			} else {
				if (patchFeedDto.getImagePath() != null) {
					throw new BusinessLogicException(ExceptionCode.BAD_IMAGE_PATH);
				} else {
					List<String> newImagePaths = awsS3Service.uploadImageToS3(multipartFile, feedId);
					updatedFeed.addImagePaths(newImagePaths.get(0), newImagePaths.get(1));
					return updatedFeed;
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Feed updateView(Long feedId) {

		Feed findFeed = findVerifiedFeed(feedId);
		findFeed.addView();
		return feedRepository.save(findFeed);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentLike> isLikedComments(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		return findFeed.getComments().stream()
			.map(comment -> commentLikeQueryRepository.findCommentLikeByFeedAndUser(comment, findUser))
			// .sorted(Comparator.comparing(a -> a.getFeed().getId()))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isMyFeed(Feed feed) {

		User findUser = userService.getLoginUserReturnNull();

		if (findUser != null) {
			return feed.getUser().equals(findUser);
		} else {
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isSavedFeed(Feed feed) {

		User findUser = userService.getLoginUserReturnNull();

		if (findUser != null) {
			return bookmarkRepository.existsByFeedAndUser(feed, findUser);
		} else {
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CustomPage<Feed> findNewestFeeds(int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findNewestFeedList(pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feed> findCategoryFeeds(String categoryName, String sort, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		if (categoryName.equals("ALL") && sort.equals("new")) {
			return feedQueryRepository.findNewestFeedList(pageRequest);
		} else {
			return feedQueryRepository.findCategoryFeedList(categoryName, sort, pageRequest);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Feed> findWeeklyBestFeeds() {
		return feedQueryRepository.findWeeklyBestFeedList();
	}

	@Override
	@Transactional
	public void deleteFeed(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {
			// commentRepository.deleteAllByFeedId(feedId);
			feedRepository.delete(findFeed);
		} else {
			throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
		}
	}

	@Override
	@Transactional
	public void deleteFeeds() {

		User findUser = userService.getLoginUser();

		Long id = findUser.getId();

		findUser.getFeeds().stream()
			.map(Feed::getId)
			.forEach(commentRepository::deleteAllByFeedId);

		feedRepository.deleteAllByUserId(id);
	}

	public Feed findFeedByFeedId(Long feedId) {
		return findVerifiedFeed(feedId);
	}

	@Override
	@Transactional(readOnly = true)
	public Feed findVerifiedFeed(Long feedId) {

		Optional<Feed> optionalFeed = feedRepository.findById(feedId);
		return optionalFeed.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
	}

	@Override
	@Transactional
	public void saveFeed(Feed feed) {
		feedRepository.save(feed);
	}
}
