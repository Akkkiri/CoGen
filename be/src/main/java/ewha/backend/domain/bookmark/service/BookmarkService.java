package ewha.backend.domain.bookmark.service;

import org.springframework.stereotype.Service;

import ewha.backend.domain.bookmark.entity.Bookmark;
import ewha.backend.domain.bookmark.repository.BookmarkRepository;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
	private final UserService userService;
	private final FeedService feedService;
	private final BookmarkRepository bookmarkRepository;

	public String saveFeed(Long feedId) {

		User findUser = userService.getLoginUser();
		Feed findFeed = feedService.findFeedByFeedId(feedId);

		if (bookmarkRepository.findByFeedAndUser(findFeed, findUser) == null) {
			Bookmark bookmark =
				Bookmark.builder()
					.feed(findFeed)
					.user(findUser)
					.build();
			bookmarkRepository.save(bookmark);

			return "saved";
		} else {
			bookmarkRepository.delete(bookmarkRepository.findByFeedAndUser(findFeed, findUser));

			return "unsaved";
		}
	}
}
