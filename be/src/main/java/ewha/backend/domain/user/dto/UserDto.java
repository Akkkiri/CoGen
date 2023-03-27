package ewha.backend.domain.user.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ewha.backend.domain.qna.entity.Qna;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Verify {
		// @NotBlank(message = "아이디를 입력해주세요.")
		// @Pattern(regexp = "[0-9a-z\\s]{6,12}", message = "6~12자의 영문, 숫자만 사용 가능합니다.")
		private String userId;
		// @NotBlank(message = "닉네임을 입력해주세요.")
		// @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣\\s]{3,20}", message = "3~20자의 한글, 영문, 숫자만 사용 가능합니다.")
		private String nickname;
		// @NotBlank(message = "패스워드를 입력해주세요.")
		// @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "8~16자 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.")
		private String password;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class VerifyResponse {
		// @NotBlank(message = "아이디를 입력해주세요.")
		// @Pattern(regexp = "[0-9a-z\\s]{6,12}", message = "6~12자의 영문, 숫자만 사용 가능합니다.")
		private String status;
		private String message;
		private String field;
		private String rejectedValue;
		private String reason;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Post {
		@NotBlank(message = "아이디를 입력해주세요.")
		@Pattern(regexp = "[0-9a-z\\s]{6,12}", message = "6~12자의 영문, 숫자만 사용 가능합니다.")
		private String userId;
		@NotBlank(message = "닉네임을 입력해주세요.")
		@Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣\\s-_]{3,20}", message = "3~20자의 한글, 영문, 숫자, 하이픈, 언더바만 사용 가능합니다.")
		private String nickname;
		@NotBlank(message = "패스워드를 입력해주세요.")
		@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "8~16자 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.")
		private String password;
		private String phoneNumber;
		private String profileImage; // 프로필 이미지
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PostResponse {
		private Long id;
		private String userId;
		private String nickname;
		private Long ariFactor;
		private List<String> role;
		private String phoneNumber;
		private String profileImage;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BasicResponse {
		private Long id;
		private String userId;
		private String nickname;
		private Long ariFactor;
		private String profileImage;
		private String thumbnailPath;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FeedCommentResponse {
		private String userId;
		private String nickname;
		private String profileImage;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInfo {
		@NotNull
		private String nickname;
		private String introduction;
		private String profileImage;
		@NotNull
		private GenderType genderType;
		@NotNull
		private AgeType ageType;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInfoResponse {
		private String userId;
		private String nickname;
		private String introduction;
		private GenderType genderType;
		private AgeType ageType;
		private Long ariFactor;
		private Long level;
		private String profileImage;
		private String thumbnailPath;
		private String phoneNumber;
		private Boolean isFirstLogin;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Password {

		@NotBlank(message = "아이디를 입력해주세요.")
		private String userId;
		@NotBlank(message = "패스워드를 입력해주세요.")
		@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "8~16자 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.")
		private String newPassword;
		@NotBlank(message = "패스워드를 입력해주세요.")
		private String newPasswordRepeat;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long id;
		private String userId;
		private String nickname;
		private GenderType genderType;
		private AgeType ageType;
		private List<Qna> qnas;
		private Long ariFactor;
		private Long level;
		private List<String> role;
		private String profileImage;
		private String thumbnailPath;
		//private ProviderType providerType; // OAuth2 반영 안함
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QnaResponse {
		private Long qnaId;
		private String content;
		private String answerBody;
	}
}
