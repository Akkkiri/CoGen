package ewha.backend.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.quiz.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
