package ewha.backend.domain.like.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LikeType likeType;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getCommentLikes().contains(this)) {
			this.user.getCommentLikes().add(this);
		}
	}

	public void addComment(Comment comment) {
		this.comment = comment;
		if (!this.comment.getCommentLikes().contains(this)) {
			this.comment.getCommentLikes().add(this);
		}
	}
}
