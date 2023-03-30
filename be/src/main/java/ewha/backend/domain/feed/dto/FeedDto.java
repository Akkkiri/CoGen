package ewha.backend.domain.feed.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ewha.backend.domain.category.entity.CategoryType;
import ewha.backend.domain.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FeedDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {

		@NotEmpty(message = "내용을 입력하셔야 합니다.")
		@Size(min = 1, max = 30, message = "30자를 넘을 수 없습니다.")
		private String title;
		@NotNull
		private CategoryType category;
		@Size(min = 3, max = 1000, message = "1000자를 넘을 수 없습니다.")
		private String body;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {

		@NotEmpty(message = "내용을 입력하셔야 합니다.")
		@Size(min = 1, max = 30, message = "30자를 넘을 수 없습니다.")
		private String title;
		@NotNull
		private CategoryType category;
		@Size(min = 3, max = 1000, message = "1000자를 넘을 수 없습니다.")
		private String body;
		private String imagePath;
	}

	@Getter
	@Builder
	public static class Like {
		private Long likeCount;

	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long feedId;
		private String category;
		private UserDto.BasicResponse userInfo;
		private String title;
		private String body;
		private Boolean isLiked;
		private Boolean isMyFeed;
		private Boolean isSavedFeed;
		private Long commentCount;
		private Long likeCount;
		private Long viewCount;
		private String imagePath;
		private String thumbnailPath;
		private Long reportCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListResponse {

		private Long feedId;
		private String category;
		private String title;
		private String body;
		private String userId;
		private Long commentCount;
		private Long likeCount;
		private Long viewCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PageResponse {

		private Long feedId;
		private String category;
		private String title;
		private String body;
		private String userId;
		private Integer commentCount;
		private Long likeCount;
		private Long viewCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BestResponse {

		private Long feedId;
		private String title;
		private String body;
		private String userId;
		private String profileImage;
		private String thumbnailPath;
		private Long commentCount;
		private Long likeCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

}
