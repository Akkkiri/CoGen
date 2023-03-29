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
		private String content;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {
		@NotBlank
		private String content;
		private String imagePath;
		private String thumbnailPath;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long questionId;
		private String content;
		private String imagePath;
		private String thumbnailPath;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PageResponse {
		private Long questionId;
		private String content;
		private AnswerDto.PageResponse answerList;
	}
}
