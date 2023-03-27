package ewha.backend.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.QuestionRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
	private final UserService userService;
	private final QuestionRepository questionRepository;

	@Transactional
	public Question createQuestion(Question question) {

		User findUser = userService.getLoginUser();

		if (findUser.getRole().contains("ADMIN")) {

			Question savedQuestion = Question.builder()
				.title(question.getTitle())
				.content(question.getContent())
				.imagePath(question.getImagePath())
				.build();

			return questionRepository.save(savedQuestion);
		} else
			throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
	}

	@Transactional
	public Question updateQuestion(Question question, Long questionId) {

		User findUser = userService.getLoginUser();

		Question findQuestion = findVerifiedQuestion(questionId);

		if (findUser.getRole().contains("ADMIN")) {

			findQuestion.updateQuestion(question);

			return questionRepository.save(findQuestion);
		} else
			throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
	}

	@Transactional(readOnly = true)
	public Question getQuestion(Long questionId) {

		Question findQuestion = findVerifiedQuestion(questionId);
		return findVerifiedQuestion(questionId);
	}

	public Question findVerifiedQuestion(Long questionId) {

		Optional<Question> optionalPairing = questionRepository.findById(questionId);
		return optionalPairing.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
	}

	public void saveQuestion(Question question) {
		questionRepository.save(question);
	}

	public void deleteQuestion(Long questionId) {

		User findUser = userService.getLoginUser();

		if (findUser.getRole().contains("ROLE_ADMIN")) {

			Question findQuestion = findVerifiedQuestion(questionId);

			questionRepository.delete(findQuestion);
		}

	}
}
