package ewha.backend.domain.user.repository;

import static ewha.backend.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.user.entity.QUser;
import ewha.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	// public void deleteNotVerifiedUsers() {
	// 	jpaQueryFactory.delete(QUser.user)
	// 		.where(QUser.user.isVerified.eq(false))
	// 		.where(QUser.user.createdAt.month().eq(LocalDateTime.now().getMonthValue() - 3))
	// 		.execute();
	// }

	public Boolean existUserByHashCode(String hashcode) {
		return jpaQueryFactory
			.selectFrom(user)
			.where(user.nickname.contains(hashcode))
			.fetchOne() != null;
	}

	public User findByProviderAndProviderId(String provider, String providerId) {
		return jpaQueryFactory
			.selectFrom(user)
			.where(user.provider.eq(provider).and(user.providerId.eq(providerId)))
			.fetchOne();
	}

	public void resetDailyFeedCount() {
		jpaQueryFactory
			.update(user)
			.set(user.dailyFeedCount, 0)
			.set(user.dailyCommentCount, 0)
			.where(user.dailyFeedCount.gt(0).or(user.dailyCommentCount.gt(0)))
			.execute();
	}

	public void resetHasQuizAndHasQuestion() {

		jpaQueryFactory
			.update(user)
			.set(user.weeklyQuestionCount, 0)
			.where(user.hasQuestion.eq(false))
			.execute();

		jpaQueryFactory
			.update(user)
			.set(user.weeklyQuizCount, 0)
			.where(user.hasQuiz.eq(false))
			.execute();

		jpaQueryFactory
			.update(user)
			.set(user.hasQuiz, false)
			.set(user.hasQuestion, false)
			.where(user.hasQuiz.eq(true).or(user.hasQuestion.eq(true)))
			.execute();
	}
}
