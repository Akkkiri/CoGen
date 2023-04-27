package ewha.backend.domain.quiz.repository;

import static ewha.backend.domain.question.entity.QQuestion.*;
import static ewha.backend.domain.quiz.entity.QQuiz.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.quiz.entity.Quiz;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuizQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<Quiz> findWeeklyQuizzes() {
		return jpaQueryFactory
			.selectFrom(quiz)
			.where(quiz.openDate.eq(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))))
			.orderBy(quiz.id.desc())
			.fetch();
	}

	public void openWeeklyQuizzes() {
		jpaQueryFactory
			.update(quiz)
			.set(quiz.isOpened, true)
			.where(quiz.openDate.eq(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))))
			.execute();
	}
}
