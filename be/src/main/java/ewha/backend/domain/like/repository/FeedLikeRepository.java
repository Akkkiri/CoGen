package ewha.backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.like.entity.FeedLike;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
}
