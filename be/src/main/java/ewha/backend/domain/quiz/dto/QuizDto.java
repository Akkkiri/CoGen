package ewha.backend.domain.quiz.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuizDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		@NotBlank
		private String answer;
		@NotBlank
		private String dummy1;
		@NotBlank
		private String dummy2;
		@NotBlank
		private String dummy3;
		@NotBlank
		private String dummy4;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		@NotBlank
		private String answer;
		@NotBlank
		private String dummy1;
		@NotBlank
		private String dummy2;
		@NotBlank
		private String dummy3;
		@NotBlank
		private String dummy4;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		@NotBlank
		private String answer;
		@NotBlank
		private String dummy1;
		@NotBlank
		private String dummy2;
		@NotBlank
		private String dummy3;
		@NotBlank
		private String dummy4;
	}
}
