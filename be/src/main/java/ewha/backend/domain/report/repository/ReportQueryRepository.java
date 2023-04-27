package ewha.backend.domain.report.repository;

import static ewha.backend.domain.report.entity.QAnswerReport.*;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.report.entity.AnswerReport;
import ewha.backend.domain.report.entity.CommentReport;
import ewha.backend.domain.report.entity.FeedReport;
import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.report.entity.QCommentReport;
import ewha.backend.domain.report.entity.QFeedReport;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public FeedReport findFeedReportByFeedAndUser(Feed findFeed, User findUser) {
		return jpaQueryFactory.selectFrom(QFeedReport.feedReport)
			.where(QFeedReport.feedReport.feed.eq(findFeed).and(QFeedReport.feedReport.user.eq(findUser)))
			.fetchFirst();
	}

	public CommentReport findCommentReportByCommentAndUser(Comment findComment, User findUser) {
		return jpaQueryFactory.selectFrom(QCommentReport.commentReport)
			.where(QCommentReport.commentReport.comment.eq(findComment).and(QCommentReport.commentReport.user.eq(findUser)))
			.fetchFirst();
	}

	public AnswerReport findAnswerReportByCommentAndUser(Answer findAnswer, User findUser) {
		return jpaQueryFactory.selectFrom(answerReport)
			.where(answerReport.answer.eq(findAnswer).and(answerReport.user.eq(findUser)))
			.fetchFirst();
	}
}
