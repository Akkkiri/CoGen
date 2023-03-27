package ewha.backend.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
