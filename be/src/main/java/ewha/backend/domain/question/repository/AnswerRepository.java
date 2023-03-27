package ewha.backend.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.question.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
