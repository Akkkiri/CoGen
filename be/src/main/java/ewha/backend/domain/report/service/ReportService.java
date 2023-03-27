package ewha.backend.domain.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.repository.CommentRepository;
import ewha.backend.domain.comment.service.CommentService;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.report.entity.CommentReport;
import ewha.backend.domain.report.entity.FeedReport;
import ewha.backend.domain.report.entity.ReportType;
import ewha.backend.domain.report.repository.CommentReportRepository;
import ewha.backend.domain.report.repository.FeedReportRepository;
import ewha.backend.domain.report.repository.ReportQueryRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	private final UserService userService;
	private final FeedService feedService;
	private final FeedRepository feedRepository;
	private final CommentService commentService;
	private final CommentRepository commentRepository;
	private final FeedReportRepository feedReportRepository;
	private final CommentReportRepository commentReportRepository;
	private final ReportQueryRepository reportQueryRepository;

	@Transactional
	public String feedReport(Long feedId) {

		User findUser = userService.getLoginUser();
		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedReport findFeedReport = reportQueryRepository.findFeedReportByFeedAndUser(findFeed, findUser);

		if (findFeedReport == null) {

			findFeedReport = FeedReport.builder()
				.reportType(ReportType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			feedReportRepository.save(findFeedReport);

			findFeed.addReportCount();

			if (findFeed.getReportCount() >= 3) {
				findFeed.setIsBlocked();
			}

			feedRepository.save(findFeed);

			return "Feed Report Added";
		} else {
			throw new BusinessLogicException(ExceptionCode.ALREADY_REPORTED);
		}
	}

	@Transactional
	public String commentReport(Long commentId) {

		User findUser = userService.getLoginUser();
		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentReport findCommentReport =
			reportQueryRepository.findCommentReportByCommentAndUser(findComment, findUser);

		if (findCommentReport == null) {

			findCommentReport = CommentReport.builder()
				.reportType(ReportType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			commentReportRepository.save(findCommentReport);

			findComment.addReportCount();

			if (findComment.getReportCount() >= 3) {
				findComment.setIsBlocked();
			}

			commentRepository.save(findComment);

			return "Comment Report Added";
		} else {
			throw new BusinessLogicException(ExceptionCode.ALREADY_REPORTED);
		}
	}
}
