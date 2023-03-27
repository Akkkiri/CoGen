package ewha.backend.domain.question.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.Nullable;

import ewha.backend.domain.image.entity.Image;
import ewha.backend.global.BaseTimeEntity;
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
public class Question extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_id")
	private Long id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String content;
	@Column
	private String imagePath;
	@Column
	private String thumbnailPath;

	@Nullable
	@JsonManagedReference
	@OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
	private Image image;

	@JsonManagedReference
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers = new ArrayList<>();

	public void updateQuestion(Question question) {
		this.title = question.getTitle();
		this.content = question.getContent();
		// this.imagePath = question.getImagePath();
		// this.thumbnailPath = question.getThumbnailPath();
	}

	public void addImagePaths(String fullPath, String thumbnailPath) {
		this.imagePath = fullPath;
		this.thumbnailPath = thumbnailPath;
	}

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
		if (answer.getQuestion() != this) {
			answer.addQuestion(this);
		}
	}
}
