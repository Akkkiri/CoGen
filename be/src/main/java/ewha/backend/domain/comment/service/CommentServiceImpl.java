package ewha.backend.domain.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.repository.CommentQueryRepository;
import ewha.backend.domain.comment.repository.CommentRepository;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.notification.entity.NotificationType;
import ewha.backend.domain.notification.service.NotificationService;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final UserService userService;
	private final UserRepository userRepository;
	private final FeedService feedService;
	private final FeedRepository feedRepository;
	private final CommentRepository commentRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final NotificationService notificationService;

	@Override
	@Transactional
	public Comment createComment(Comment comment, Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		Comment savedComment =
			Comment.builder()
				.feed(findFeed)
				.user(findUser)
				.body(comment.getBody())
				.likeCount(0L)
				.build();

		findFeed.addCommentCount();

		if (findUser.getDailyCommentCount() <= 5) {
			findUser.addAriFactor(1);
			findUser.addDailyFeedCount();

			if (findUser.getAriFactor() == 50) {
				findUser.addLevel();
			}
			userRepository.save(findUser);
		}

		if (!findUser.getId().equals(findFeed.getUser().getId())) {
			String body = "작성하신 게시글 <" + findFeed.getTitle() + ">에 "
				+ findUser.getNickname() + "님이 댓글을 남겼습니다.";
			String content = findFeed.getTitle();
			String url = "https://www.akkkiri.co.kr/post/" + findFeed.getId();
			notificationService.send(findFeed.getUser(), url, body, content, NotificationType.COMMENT);
		}

		feedRepository.save(findFeed);
		return commentRepository.save(savedComment);
	}

	@Override
	@Transactional
	public Comment updateComment(Comment comment, Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = findVerifiedComment(commentId);

		if (findUser.equals(findComment.getUser())) {

			findComment.updateComment(comment);

			return commentRepository.save(findComment);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Comment> getFeedComments(Long feedId, String sort, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return commentQueryRepository.findFeedComment(feedId, sort, pageRequest);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> isMyComments(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findFeedByFeedId(feedId);

		return findFeed.getComments().stream()
			.filter(comment -> comment.getUser().equals(findUser))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteComment(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = findVerifiedComment(commentId);

		Long feedId = findComment.getFeed().getId();

		if (findUser.equals(findComment.getUser())) {
			commentRepository.delete(findComment);
			Feed findFeed = feedService.findVerifiedFeed(feedId);
			findFeed.removeCommentCount();
			feedRepository.save(findFeed);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	@Override
	@Transactional
	public void deleteComments(Long commentId) {

		User findUser = userService.getLoginUser();

		Long id = findUser.getId();

		commentRepository.deleteAllByUserId(id);

		Comment findComment = findVerifiedComment(commentId);
	}

	@Override
	@Transactional(readOnly = true)
	public Comment findVerifiedComment(long commentId) {

		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		return optionalComment.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
	}

}
