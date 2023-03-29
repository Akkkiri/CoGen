package ewha.backend.domain.like.repository;

import static ewha.backend.domain.like.entity.QAnswerLike.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.like.entity.AnswerLike;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerLikeQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public AnswerLike findAnswerLikeByAnswerAndUser(Answer findAnswer, User findUser) {

		return jpaQueryFactory.selectFrom(answerLike)
			.where(answerLike.answer.eq(findAnswer).and(answerLike.user.eq(findUser)))
			.fetchFirst();
	}
}
