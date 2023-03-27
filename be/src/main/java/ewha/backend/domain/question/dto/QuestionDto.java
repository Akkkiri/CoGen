package ewha.backend.domain.question.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuestionDto {

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Post {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		@NotBlank
		private String answerBody;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		private String imagePath;
		private String thumbnailPath;
		@NotBlank
		private String answerBody;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long questionId;
		private String title;
		private String content;
		private String imagePath;
		private String thumbnailPath;
		private String answerBody;
	}
}
