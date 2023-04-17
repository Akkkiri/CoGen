package ewha.backend.domain.quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.domain.quiz.repository.QuizQueryRepository;
import ewha.backend.domain.quiz.repository.QuizRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final UserService userService;
	private final UserRepository userRepository;
	private final QuizRepository quizRepository;
	private final QuizQueryRepository quizQueryRepository;

	@Transactional
	public Quiz createQuiz(Quiz quiz) {

		return quizRepository.save(quiz);
	}

	@Transactional
	public Quiz updateQuiz(Long quizId, Quiz quiz) {

		Quiz findQuiz = findVerifiedQuiz(quizId);
		Quiz updatedQuiz = findQuiz.update(quiz);

		return quizRepository.save(updatedQuiz);
	}

	@Transactional(readOnly = true)
	public List<Quiz> findWeeklyQuizzes() {
		return quizQueryRepository.findWeeklyQuizzes();
	}

	@Transactional
	public String clearWeeklyQuizzes() {
		User findUser = userService.getLoginUserReturnNull();
		if (findUser == null) {
			return "completed.";
		} else {
			if (!findUser.getHasQuiz()) {
				findUser.addAriFactor(3);
				findUser.setHasQuiz(true);
				findUser.addWeeklyQuizCount();

				if (findUser.getWeeklyQuizCount() == 4) {
					findUser.addAriFactor(7);
					findUser.setWeeklyQuizCount(0);
				}

				if (findUser.getAriFactor() >= 50) {
					findUser.addLevel(findUser.getAriFactor());
				}
				userRepository.save(findUser);
			}
			return "completed.";
		}
	}

	@Transactional
	public void deleteQuiz(Long quizId) {

		Quiz findQuiz = findVerifiedQuiz(quizId);
		quizRepository.delete(findQuiz);
	}

	@Transactional(readOnly = true)
	public Quiz findVerifiedQuiz(Long quizId) {
		return quizRepository.findById(quizId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUIZ_NOT_FOUND));
	}
}
