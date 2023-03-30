package ewha.backend.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.repository.CommentRepository;
import ewha.backend.domain.comment.service.CommentService;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.like.entity.AnswerLike;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.domain.like.entity.FeedLike;
import ewha.backend.domain.like.entity.LikeType;
import ewha.backend.domain.like.repository.AnswerLikeQueryRepository;
import ewha.backend.domain.like.repository.AnswerLikeRepository;
import ewha.backend.domain.like.repository.CommentLikeQueryRepository;
import ewha.backend.domain.like.repository.CommentLikeRepository;
import ewha.backend.domain.like.repository.FeedLikeQueryRepository;
import ewha.backend.domain.like.repository.FeedLikeRepository;
import ewha.backend.domain.notification.entity.NotificationType;
import ewha.backend.domain.notification.service.NotificationService;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.repository.AnswerRepository;
import ewha.backend.domain.question.service.AnswerService;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
	private final UserService userService;
	private final FeedService feedService;
	private final FeedRepository feedRepository;
	private final CommentService commentService;
	private final CommentRepository commentRepository;
	private final FeedLikeRepository feedLikeRepository;
	private final FeedLikeQueryRepository feedLikeQueryRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final CommentLikeQueryRepository commentLikeQueryRepository;
	private final AnswerService answerService;
	private final AnswerRepository answerRepository;
	private final AnswerLikeRepository answerLikeRepository;
	private final AnswerLikeQueryRepository answerLikeQueryRepository;
	// private final LikeRepository likeRepository;
	// private final LikeQueryRepository likeQueryRepository;
	private final NotificationService notificationService;

	@Override
	@Transactional
	public String feedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			findFeedLike = FeedLike.builder()
				.likeType(LikeType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			feedLikeRepository.save(findFeedLike);

			findFeed.addLike();

			feedRepository.save(findFeed);

			if (!findUser.getId().equals(findFeed.getUser().getId())) {
				String body = "작성하신 피드 <" + findFeed.getTitle() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findFeed.getTitle();
				String url = "http://localhost:8080/feeds/" + findFeed.getId();
				notificationService.send(findFeed.getUser(), url, body, content, NotificationType.LIKE);
			}

			return "Feed Like Added";
			// return findFeed;
		} else {

			feedLikeRepository.delete(findFeedLike);

			findFeed.removeLike();

			feedRepository.save(findFeed);

			return "Feed Like Removed";
			// return findFeed;
		}
	}

	@Override
	@Transactional
	public String commentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {
			findCommentLike = CommentLike.builder()
				.likeType(LikeType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			commentLikeRepository.save(findCommentLike);

			findComment.addLike();

			commentRepository.save(findComment);

			if (!findUser.getId().equals(findComment.getUser().getId())) {
				String body = "작성하신 댓글 <" + findComment.getBody() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findComment.getBody();
				String url = "http://localhost:8080/comments/" + findComment.getId();
				notificationService.send(findComment.getUser(), url, body, content, NotificationType.LIKE);
			}

			return "Comment Like Added";
			// return findComment;
		} else {

			commentLikeRepository.delete(findCommentLike);

			findComment.removeLike();

			commentRepository.save(findComment);

			return "Comment Like Removed";
			// return findComment;
		}
	}

	@Override
	@Transactional
	public String answerLike(Long answerId) {

		User findUser = userService.getLoginUser();

		Answer findAnswer = answerService.findVerifiedAnswer(answerId);

		AnswerLike findAnswerLike = answerLikeQueryRepository.findAnswerLikeByAnswerAndUser(findAnswer, findUser);

		if (findAnswerLike == null) {
			findAnswerLike = AnswerLike.builder()
				.likeType(LikeType.ANSWER)
				.user(findUser)
				.answer(findAnswer)
				.build();

			answerLikeRepository.save(findAnswerLike);

			findAnswer.addLike();

			answerRepository.save(findAnswer);

			if (!findUser.getId().equals(findAnswer.getUser().getId())) {
				String body = "작성하신 피드 <" + findAnswer.getAnswerBody() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findAnswer.getAnswerBody();
				String url = "http://localhost:8080/feeds/" + findAnswer.getId();
				notificationService.send(findAnswer.getUser(), url, body, content, NotificationType.LIKE);
			}

			return "Feed Like Added";
			// return findFeed;
		} else {

			answerLikeRepository.delete(findAnswerLike);

			findAnswer.removeLike();

			answerRepository.save(findAnswer);

			return "Feed Like Removed";
		}
	}

	@Override
	@Transactional
	public Feed createFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			findFeedLike = FeedLike.builder()
				.likeType(LikeType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			feedLikeRepository.save(findFeedLike);

			findFeed.addLike();

			if (!findUser.getId().equals(findFeed.getUser().getId())) {
				String body = "작성하신 피드 <" + findFeed.getTitle() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findFeed.getTitle();
				String url = "http://localhost:8080/feeds/" + findFeed.getId();
				notificationService.send(findFeed.getUser(), url, body, content, NotificationType.LIKE);
			}

			return findFeed;
		} else {
			throw new BusinessLogicException(ExceptionCode.LIKED);
		}
	}

	@Override
	@Transactional
	public Feed deleteFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			feedLikeRepository.delete(findFeedLike);
		}

		findFeed.removeLike();

		return findFeed;
	}

	@Override
	@Transactional
	public Comment createCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {
			findCommentLike = CommentLike.builder()
				.likeType(LikeType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			commentLikeRepository.save(findCommentLike);

			findComment.addLike();

			if (!findUser.getId().equals(findComment.getUser().getId())) {
				String body = "작성하신 댓글 <" + findComment.getBody() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findComment.getBody();
				String url = "http://localhost:8080/comments/" + findComment.getId();
				notificationService.send(findComment.getUser(), url, body, content, NotificationType.LIKE);
			}

			return findComment;
		} else {
			throw new BusinessLogicException(ExceptionCode.LIKED);
		}
	}

	@Override
	@Transactional
	public Comment deleteCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {

			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			commentLikeRepository.delete(findCommentLike);
		}

		findComment.removeLike();

		return findComment;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isLikedFeed(Feed feed) {

		User findUser = userService.getLoginUserReturnNull();

		if (findUser != null) {
			return feedLikeQueryRepository.findFeedLikeByFeedAndUser(feed, findUser) != null;
		} else {
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isLikedComment(Comment comment) {

		User findUser = userService.getLoginUserReturnNull();

		if (findUser != null) {
			return commentLikeQueryRepository.findCommentLikeByFeedAndUser(comment, findUser) != null;
		} else {
			return false;
		}

	}
}
