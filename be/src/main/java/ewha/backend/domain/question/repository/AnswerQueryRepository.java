package ewha.backend.domain.question.repository;

import static ewha.backend.domain.question.entity.QAnswer.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<Answer> findMyQuestionAnswer(User findUser, Question findQuestion) {

		return jpaQueryFactory
			.selectFrom(answer)
			.where(answer.question.eq(findQuestion).and(answer.user.eq(findUser)))
			.orderBy(answer.id.desc())
			.fetch();
	}

	public List<Answer> findUserQuestionAnswer(Long userId, Question findQuestion) {

		return jpaQueryFactory
			.selectFrom(answer)
			.where(answer.question.eq(findQuestion).and(answer.user.id.eq(userId)))
			.orderBy(answer.id.desc())
			.fetch();
	}

	public Page<Answer> findQuestionAnswers(Long questionId, String sort, Pageable pageable) {

		List<Answer> answerList = new ArrayList<>();

		if (sort.equals("new")) {
			answerList = jpaQueryFactory
				.selectFrom(answer)
				.where(answer.question.id.eq(questionId))
				.orderBy(answer.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else if (sort.equals("likes")) {
			answerList = jpaQueryFactory
				.selectFrom(answer)
				.where(answer.question.id.eq(questionId))
				.orderBy(answer.likeCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		}

		Long total = jpaQueryFactory
			.select(answer.count())
			.from(answer)
			.where(answer.question.id.eq(questionId))
			.fetchOne();

		return new PageImpl<>(answerList, pageable, total);
	}
}
