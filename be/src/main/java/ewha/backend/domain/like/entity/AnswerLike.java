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

import com.fasterxml.jackson.annotation.JsonBackReference;

import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.user.entity.User;
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
public class AnswerLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "answer_like_id")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LikeType likeType;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "answer_id")
	private Answer answer;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getAnswerLikes().contains(this)) {
			this.user.getAnswerLikes().add(this);
		}
	}

	public void addAnswer(Answer answer) {
		this.answer = answer;
		if (!this.answer.getAnswerLikes().contains(this)) {
			this.answer.getAnswerLikes().add(this);
		}
	}
}
