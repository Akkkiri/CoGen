package ewha.backend.global.aop;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.question.repository.QuestionQueryRepository;
import ewha.backend.domain.quiz.repository.QuizQueryRepository;
import ewha.backend.domain.user.repository.UserQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

	private final UserQueryRepository userQueryRepository;
	private final QuestionQueryRepository questionQueryRepository;
	private final QuizQueryRepository quizQueryRepository;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void resetHasDailyFeed() {
		userQueryRepository.resetDailyFeedCount();
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * MON")
	public void resetHasQuizAndHasQuestion() {
		userQueryRepository.resetHasQuizAndHasQuestion();
		questionQueryRepository.openWeeklyQuestion();
		quizQueryRepository.openWeeklyQuizzes();
	}
}
