package ewha.backend.domain.quiz.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ewha.backend.global.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quiz_id")
	private Long id;
	@Column
	private String content;
	@Column
	private String answer;
	@Column
	private String explanation;
	@Column
	private String dummy1;
	@Column
	private String dummy2;
	@Column
	private LocalDate openDate;
	// private Integer openWeek;
	@Column
	private Boolean isOpened;

	public Quiz update(Quiz quiz) {
		this.content = quiz.getContent();
		this.answer = quiz.getAnswer();
		this.dummy1 = quiz.getDummy1();
		this.dummy2 = quiz.getDummy2();
		this.explanation = quiz.getExplanation();

		return this;
	}
}
