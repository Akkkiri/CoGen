package ewha.backend.domain.like.repository;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.like.entity.QCommentLike;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentLikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public CommentLike findCommentLikeByFeedAndUser(Comment findComment, User findUser) {

		return jpaQueryFactory.selectFrom(QCommentLike.commentLike)
			.where(QCommentLike.commentLike.comment.eq(findComment).and(QCommentLike.commentLike.user.eq(findUser)))
			.fetchFirst();
	}
}
