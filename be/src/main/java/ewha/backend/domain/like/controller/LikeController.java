package ewha.backend.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.like.service.LikeService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {
	private final LikeService likeService;

	@PatchMapping("/feeds/{feed_id}/like")
	public ResponseEntity<String> feedLike(@PathVariable("feed_id") Long feedId) {

		String response = likeService.feedLike(feedId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/comments/{comment_id}/like")
	public ResponseEntity<String> commentLike(@PathVariable("comment_id") Long commentId) {

		String response = likeService.commentLike(commentId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/answers/{answer_id}/like")
	public ResponseEntity<String> answerLike(@PathVariable("answer_id") Long answerId) {

		String response = likeService.answerLike(answerId);

		return ResponseEntity.ok(response);
	}

	// @PatchMapping("/feeds/{feed_id}/like")
	// public ResponseEntity<HttpStatus> postFeedLike(@PathVariable("feed_id") Long feedId) {
	//
	// 	Feed likedFeed = likeService.createFeedLike(feedId);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).build();
	// }

	@PatchMapping("/feeds/{feed_id}/dislike")
	public ResponseEntity<HttpStatus> deleteFeedLike(@PathVariable("feed_id") Long feedId) {

		Feed dislikedFeed = likeService.deleteFeedLike(feedId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// @PatchMapping("/comments/{comment_id}/like")
	// public ResponseEntity<HttpStatus> postCommentLike(@PathVariable("comment_id") Long commentId) {
	//
	// 	Comment likedComment = likeService.createCommentLike(commentId);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).build();
	// }

	@PatchMapping("/comments/{comment_id}/dislike")
	public ResponseEntity<HttpStatus> deleteCommentLike(@PathVariable("comment_id") Long commentId) {

		Comment dislikedComment = likeService.deleteCommentLike(commentId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
