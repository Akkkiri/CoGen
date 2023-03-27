package ewha.backend.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.comment.entity.QComment;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Comment> findCommentListByUser(User user, Pageable pageable) {
		List<Comment> commentList = jpaQueryFactory
			.select(QComment.comment)
			.from(QComment.comment)
			.where(QComment.comment.user.eq(user))
			.orderBy(QComment.comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QComment.comment.count())
			.from(QComment.comment)
			.fetchOne();

		return new PageImpl<>(commentList, pageable, total);
	}

	public Page<Comment> findFeedComment(Long feedId, Pageable pageable) {
		List<Comment> commentList = jpaQueryFactory
			.selectFrom(QComment.comment)
			.where(QComment.comment.feed.id.eq(feedId))
			.orderBy(QComment.comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QComment.comment.count())
			.from(QComment.comment)
			.fetchOne();

		return new PageImpl<>(commentList, pageable, total);
	}
}
