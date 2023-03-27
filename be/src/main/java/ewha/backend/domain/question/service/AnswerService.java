package ewha.backend.domain.question.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.AnswerRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerRepository answerRepository;

	public Answer postAnswer(Answer answer, Long questionId) {

		User findUser = userService.getLoginUser();

		Question question = questionService.findVerifiedQuestion(questionId);

		Answer savedAnswer =
			Answer.builder()
				.answerBody(answer.getAnswerBody())
				.user(findUser)
				.question(question)
				.build();

		return answerRepository.save(savedAnswer);
	}

	public Answer patchAnswer(Answer answer, Long answerId) {

		User findUser = userService.getLoginUser();

		Answer findAnswer = findVerifiedAnswer(answerId);

		findAnswer.updateAnswer(answer);

		return answerRepository.save(findAnswer);
	}

	public void deleteAnswer(Long answerId) {

		User findUser = userService.getLoginUser();

		Answer findAnswer = findVerifiedAnswer(answerId);

		if (findUser.getId().equals(findAnswer.getUser().getId())) {
			answerRepository.delete(findAnswer);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	public Answer findVerifiedAnswer(Long answerId) {
		return answerRepository.findById(answerId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
	}
}
