package ewha.backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.like.entity.AnswerLike;

public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {
}
