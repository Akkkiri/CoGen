package ewha.backend.domain.like.service;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.question.entity.Answer;

public interface LikeService {

	String feedLike(Long feedId);

	String commentLike(Long commentId);

	String answerLike(Long answerId);

	Feed createFeedLike(Long feedId);

	Feed deleteFeedLike(Long feedId);

	Comment createCommentLike(Long commentId);

	Comment deleteCommentLike(Long commentId);

	Boolean isLikedFeed(Feed feed);

	Boolean isLikedComment(Comment comment);
	Boolean isLikedAnswer(Answer answer);
}
