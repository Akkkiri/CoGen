package ewha.backend.domain.image.entity;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.question.entity.Question;
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
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;

	@Column
	@Enumerated(EnumType.STRING)
	private ImageType imageType;

	@Column(nullable = false)
	private String originalImageName;

	@Column(nullable = false)
	private String storedImageName;

	@Column(nullable = false)
	private String storedPath;

	@Column(nullable = false)
	private String thumbnailPath;

	@JsonBackReference
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "user_id")
	private User user;

	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "question_id")
	private Question question;
}
