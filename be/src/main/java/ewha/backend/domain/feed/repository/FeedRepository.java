package ewha.backend.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ewha.backend.domain.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM FEED WHERE ID = :id")
	void deleteAllByUserId(Long id);
}
