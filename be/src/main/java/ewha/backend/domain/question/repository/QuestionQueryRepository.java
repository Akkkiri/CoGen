package ewha.backend.domain.question.repository;

import static ewha.backend.domain.question.entity.QAnswer.*;
import static ewha.backend.domain.question.entity.QQuestion.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Question> findMyQuestions(User findUser, Pageable pageable) {

		List<Question> questionList = jpaQueryFactory
			.selectFrom(question)
			.join(question.answers, answer)
			.where(question.isOpened.eq(true).and(answer.user.eq(findUser).count().gt(0)))
			.orderBy(question.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(question.count())
			.from(question)
			.join(question.answers, answer)
			.where(question.isOpened.eq(true).and(answer.user.eq(findUser).count().gt(0)))
			.fetchOne();

		return new PageImpl<>(questionList, pageable, total);
	}

	public Page<Question> findUserQuestions(User findUser, Pageable pageable) {

		List<Question> questionList = jpaQueryFactory
			.selectFrom(question)
			.join(question.answers, answer)
			.where(answer.user.eq(findUser).count().gt(0))
			.orderBy(question.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(question.count())
			.from(question)
			.join(question.answers, answer)
			.where(answer.user.eq(findUser).count().gt(0))
			.fetchOne();

		return new PageImpl<>(questionList, pageable, total);
	}

	public Question findQuestionOfWeek() {
		return jpaQueryFactory
			.selectFrom(question)
			.where(question.openDate.eq(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))))
			.fetchOne();
	}

	public Page<Question> findPassedQuestions(Pageable pageable) {

		List<Question> questionList = jpaQueryFactory
			.selectFrom(question)
			.where(question.isOpened.eq(true))
			.orderBy(question.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(question.count())
			.from(question)
			.where(question.isOpened.eq(true))
			.fetchOne();

		return new PageImpl<>(questionList, pageable, total);
	}

	public void openWeeklyQuestion() {
		jpaQueryFactory
			.update(question)
			.set(question.isOpened, true)
			.where(question.openDate.eq(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))))
			.execute();
	}
}
