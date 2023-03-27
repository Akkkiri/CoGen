package ewha.backend.domain.follow.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ewha.backend.domain.follow.entity.Follow;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.follow.entity.QFollow;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Follow findFollowByUserIds(Long followingUserId, Long followedUserId) {

		return jpaQueryFactory.selectFrom(QFollow.follow)
			.where(QFollow.follow.followingUser.id.eq(followingUserId).and(QFollow.follow.followedUser.id.eq(followedUserId)))
			.fetchFirst();
	}

	public Page<User> findFollowersByUserId(Long userId, Pageable pageable) {

		List<User> followList =
			jpaQueryFactory.select(QFollow.follow.followingUser)
				.from(QFollow.follow)
				// .join(follow.followingUser, user)
				.where(QFollow.follow.followedUser.id.eq(userId))
				.orderBy(QFollow.follow.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total =
			jpaQueryFactory.select(QFollow.follow.followingUser.count())
				.from(QFollow.follow)
				// .join(follow.followingUser, user)
				.where(QFollow.follow.followedUser.id.eq(userId))
				.fetchOne();

		return new PageImpl<>(followList, pageable, total);
	}

	public Page<User> findFollowersWithLoginUserByUserId(Long loginUserId, Long userId, Pageable pageable) {

		User loginUser =
			jpaQueryFactory.select(QFollow.follow.followingUser)
				.from(QFollow.follow)
				.where(QFollow.follow.followingUser.id.eq(loginUserId).and(QFollow.follow.followedUser.id.eq(userId)))
				.fetchFirst();

		List<User> followList =
			jpaQueryFactory.select(QFollow.follow.followingUser)
				.from(QFollow.follow)
				// .join(follow.followingUser, user)
				.where(QFollow.follow.followingUser.id.ne(loginUserId))
				.where(QFollow.follow.followedUser.id.eq(userId))
				.orderBy(QFollow.follow.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total =
			jpaQueryFactory.select(QFollow.follow.followingUser.count())
				.from(QFollow.follow)
				// .join(follow.followingUser, user)
				.where(QFollow.follow.followedUser.id.eq(userId))
				.fetchOne();

		if (loginUser != null) {
			followList.add(0, loginUser);
			total = total + 1;
			return new PageImpl<>(followList, pageable, total);
		} else {
			return new PageImpl<>(followList, pageable, total);
		}
	}

	public Page<User> findFollowingsByUserId(Long userId, Pageable pageable) {

		List<User> followList =
			jpaQueryFactory.select(QFollow.follow.followedUser)
				.from(QFollow.follow)
				.where(QFollow.follow.followingUser.id.eq(userId))
				.orderBy(QFollow.follow.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total =
			jpaQueryFactory.select(QFollow.follow.followedUser.count())
				.from(QFollow.follow)
				.where(QFollow.follow.followingUser.id.eq(userId))
				.fetchOne();

		return new PageImpl<>(followList, pageable, total);
	}
}
