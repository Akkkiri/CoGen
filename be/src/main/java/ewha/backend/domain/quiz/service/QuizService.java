package ewha.backend.domain.quiz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.domain.quiz.repository.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final QuizRepository quizRepository;

	@Transactional
	public Quiz createQuiz(Quiz quiz) {

		return quizRepository.save(quiz);
	}
}
