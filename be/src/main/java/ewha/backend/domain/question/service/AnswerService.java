package ewha.backend.domain.question.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.AnswerQueryRepository;
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
	private final AnswerQueryRepository answerQueryRepository;

	public Answer postAnswer(Answer answer, Long questionId) {

		User findUser = userService.getLoginUser();

		Question question = questionService.findAnswerableQuestion(questionId);

		Answer savedAnswer =
			Answer.builder()
				.answerBody(answer.getAnswerBody())
				.user(findUser)
				.question(question)
				.likeCount(0L)
				.build();

		if (!findUser.getHasQuestion()) {
			findUser.addAriFactor(5);
			findUser.setHasQuestion(true);
			findUser.addWeeklyQuestionCount();

			if (findUser.getWeeklyQuestionCount() == 4) {
				findUser.addAriFactor(10);
				findUser.setWeeklyQuestionCount(0);
			}

			if (findUser.getAriFactor() >= 50) {
				findUser.addLevel(findUser.getAriFactor());
			}
		}

		return answerRepository.save(savedAnswer);
	}

	public Answer patchAnswer(Answer answer, Long answerId) {

		User findUser = userService.getLoginUser();

		Answer findAnswer = findVerifiedAnswer(answerId);

		Question question = questionService.findAnswerableQuestion(findAnswer.getQuestion().getId());

		if (findAnswer.getUser().equals(findUser)) {
			findAnswer.updateAnswer(answer);
			return answerRepository.save(findAnswer);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	public Page<Answer> findQuestionAnswers(Long questionId, String sort, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return answerQueryRepository.findQuestionAnswers(questionId, sort, pageRequest);
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
