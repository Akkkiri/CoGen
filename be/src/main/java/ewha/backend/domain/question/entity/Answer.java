package ewha.backend.domain.question.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "answer_id")
	private Long id;
	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String answerBody;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getAnswers().contains(this)) {
			this.user.getAnswers().add(this);
		}
	}

	public void addQuestion(Question question) {
		this.question = question;
		if (!this.question.getAnswers().contains(this)) {
			this.question.getAnswers().add(this);
		}
	}

	public void updateAnswer(Answer answer) {
		this.answerBody = answer.getAnswerBody();
	}
}
