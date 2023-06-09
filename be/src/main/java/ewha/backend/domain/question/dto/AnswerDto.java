package ewha.backend.domain.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.context.annotation.Primary;

import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnswerDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Post {
		@NotBlank
		private String answerBody;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Patch {
		@NotBlank
		private String answerBody;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {

		private Long answerId;
		private Long userId;
		private String nickname;
		private String hashcode;
		private GenderType genderType;
		private AgeType ageType;
		private String profileImage;
		private String thumbnailPath;
		private String answerBody;
		private Long likeCount;
		private Long reportCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;

	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ListResponse {
		private Long answerId;
		private Long userId;
		private String nickname;
		private String hashcode;
		private GenderType genderType;
		private AgeType ageType;
		private String profileImage;
		private String thumbnailPath;
		private String answerBody;
		private Boolean isLiked;
		private Long likeCount;
		private Long reportCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class PageResponse {

		private List<String> answerBody;
	}
}
