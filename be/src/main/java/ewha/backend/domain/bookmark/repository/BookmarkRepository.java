package ewha.backend.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.bookmark.entity.Bookmark;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Bookmark findByFeedAndUser(Feed feed, User user);

	Boolean existsByFeedAndUser(Feed feed, User user);
}
