package ewha.backend.domain.feed.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.global.config.CustomPage;

public interface FeedService {
	Feed createFeed(Feed feed);

	Feed updateFeed(Feed feed, Long feedId);

	Feed updateView(Long feedId);

	List<CommentLike> isLikedComments(Long feedId);

	Boolean isMyFeed(Feed feed);

	Boolean isSavedFeed(Feed feed);

	CustomPage<Feed> findNewestFeeds(int page);

	Page<Feed> findCategoryFeeds(String categoryName, int page);

	void deleteFeed(Long feedId);

	void deleteFeeds();

	Feed findFeedByFeedId(Long feedId);

	Feed findVerifiedFeed(Long feedId);

	void saveFeed(Feed feed);
}
