package ewha.backend.domain.like.repository;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.like.entity.FeedLike;
import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.like.entity.QFeedLike;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedLikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public FeedLike findFeedLikeByFeedAndUser(Feed findFeed, User findUser) {

		return jpaQueryFactory.selectFrom(QFeedLike.feedLike)
			.where(QFeedLike.feedLike.feed.eq(findFeed).and(QFeedLike.feedLike.user.eq(findUser)))
			.fetchFirst();
	}
}
