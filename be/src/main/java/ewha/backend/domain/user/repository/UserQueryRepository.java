package ewha.backend.domain.user.repository;

import static ewha.backend.domain.user.entity.QUser.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

	public Page<User> findUserPageByNickname(String query, Pageable pageable) {

		List<User> exactUserList = jpaQueryFactory.selectFrom(user)
			.where(user.nickname.like(query + "#%"))
			.orderBy(user.level.desc(), user.ariFactor.desc(), user.createdAt.desc())
			.fetch();

		List<User> userList = jpaQueryFactory
			.selectFrom(user)
			.where(user.nickname.contains(query))
			.orderBy(user.nickname.asc(), user.level.desc(), user.ariFactor.desc(), user.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(user.nickname.contains(query))
			.fetchOne();

		if (exactUserList.size() != 0) {
			exactUserList.addAll(userList);
			exactUserList.stream().distinct().collect(Collectors.toList());
			return new PageImpl<>(exactUserList, pageable, total);
		} else {
			return new PageImpl<>(userList, pageable, total);
		}
	}

	public Page<User> findUserPageByHashcode(String query, Pageable pageable) {

		User exactUser = jpaQueryFactory.selectFrom(user)
			.where(user.nickname.like("%#" + query))
			.fetchOne();

		List<User> userList = jpaQueryFactory
			.selectFrom(user)
			.where(user.nickname.like("%#%" + query + "%"))
			.orderBy(user.nickname.asc(), user.level.desc(), user.ariFactor.desc(), user.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(user.nickname.contains("%#%" + query + "%"))
			.fetchOne();

		if (exactUser != null) {
			userList.add(0, exactUser);
			return new PageImpl<>(userList, pageable, total);
		} else {
			return new PageImpl<>(userList, pageable, total);
		}
	}
}
