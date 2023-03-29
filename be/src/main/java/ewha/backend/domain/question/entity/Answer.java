package ewha.backend.domain.question.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ewha.backend.domain.like.entity.AnswerLike;
import ewha.backend.domain.report.entity.AnswerReport;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	@Column
	private Long likeCount;
	@Column
	private Long reportCount;
	@Column
	private Boolean isBlocked;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;
	@JsonManagedReference
	@OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
	private List<AnswerLike> answerLikes = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
	private List<AnswerReport> answerReports = new ArrayList<>();

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

	public void addAnswerLike(AnswerLike answerLike) {
		this.answerLikes.add(answerLike);
		if (answerLike.getAnswer() != this) {
			answerLike.addAnswer(this);
		}
	}

	public void addAnswerReport(AnswerReport answerReport) {
		this.answerReports.add(answerReport);
		if (answerReport.getAnswer() != this) {
			answerReport.addAnswer(this);
		}
	}

	public void updateAnswer(Answer answer) {
		this.answerBody = answer.getAnswerBody();
	}

	public void addLike() {
		if (likeCount == null) {
			this.likeCount = 1L;
		} else {
			this.likeCount = likeCount + 1;
		}
	}

	public void removeLike() {
		if (likeCount > 0) {
			this.likeCount = likeCount - 1;
		}
	}

	public void addReportCount() {
		this.reportCount++;
	}

	public void removeReportCount() {
		if (this.reportCount > 0) {
			this.reportCount--;
		}
	}

	public void setIsBlocked() {
		isBlocked = true;
	}
}
