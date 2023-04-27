package ewha.backend.domain.quiz.dto;

import java.util.List;

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
		private String content;
		@NotBlank
		private String explanation;
		@NotBlank
		private String answer;
		@NotBlank
		private String dummy1;
		@NotBlank
		private String dummy2;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {
		@NotBlank
		private String content;
		@NotBlank
		private String explanation;
		@NotBlank
		private String answer;
		@NotBlank
		private String dummy1;
		@NotBlank
		private String dummy2;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private String content;
		private List<String> bodies;
		private String answer;
		private String explanation;
	}
}
