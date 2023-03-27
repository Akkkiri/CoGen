package ewha.backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.like.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
