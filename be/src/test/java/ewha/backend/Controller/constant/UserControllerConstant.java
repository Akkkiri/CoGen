package ewha.backend.Controller.constant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.category.entity.CategoryType;
import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;
import ewha.backend.global.security.dto.LoginDto;

public class UserControllerConstant {

	public static final UserDto.Post POST_USER_DTO =
		UserDto.Post.builder()
			.userId("01012345678")
			.nickname("닉네임")
			.password("12345678a")
			.passwordRepeat("12345678a")
			.build();

	public static final UserDto.Post PASSWORD_NOT_MATCH_POST_USER_DTO =
		UserDto.Post.builder()
			.userId("01012345678")
			.nickname("닉네임")
			.password("12345678a")
			.passwordRepeat("12345678")
			.build();

	public static final UserDto.PostResponse POST_USER_RESPONSE_DTO =
		UserDto.PostResponse.builder()
			.id(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.level(1)
			.ariFactor(10)
			.role(List.of("ROLE_USER"))
			.build();

	public static final LoginDto.PatchDto LOGIN_PATCH_USER_DTO =
		LoginDto.PatchDto.builder()
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.build();

	public static final LoginDto.QnaDto QNA_DTO =
		LoginDto.QnaDto.builder()
			.qnaId(1L)
			.answerBody("답변")
			.build();

	public static final List<LoginDto.QnaDto> QNA_DTO_LIST =
		List.of(QNA_DTO, QNA_DTO);

	public static final UserDto.Response USER_RESPONSE_DTO =
		UserDto.Response.builder()
			.id(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.hashcode("#123456")
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.level(1)
			.ariFactor(10)
			.role(List.of("ROLE_USER"))
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.build();

	public static final UserDto.UserInfo PATCH_USER_DTO =
		UserDto.UserInfo.builder()
			.nickname("닉네임")
			.profileImage("profile Image")
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.build();

	public static final UserDto.Password CHANGE_PASSWORD_DTO =
		UserDto.Password.builder()
			.userId("01012345678")
			.newPassword("12345678a")
			.newPasswordRepeat("12345678a")
			.build();

	public static final UserDto.MyPageResponse MY_PAGE_INFO_RESPONSE_DTO =
		UserDto.MyPageResponse.builder()
			.userId("01012345678")
			.nickname("닉네임")
			.hashcode("#123456")
			.friendsNum(10L)
			.level(1)
			.ariFactor(10)
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.TWENTIES)
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.build();

	public static final UserDto.UserPageResponse USER_PAGE_INFO_RESPONSE_DTO =
		UserDto.UserPageResponse.builder()
			.userId("01012345678")
			.nickname("닉네임")
			.hashcode("#123456")
			.friendsNum(10L)
			.level(1)
			.ariFactor(10)
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.build();

	public static final UserDto.QnaResponse MY_QNA_RESPONSE =
		UserDto.QnaResponse.builder()
			.qnaId(1L)
			.content("QnA 질문")
			.answerBody("QnA 답변")
			.build();

	public static final List<UserDto.QnaResponse> MY_QNA_RESPONSE_LIST =
		new ArrayList<>(
			List.of(
				MY_QNA_RESPONSE, MY_QNA_RESPONSE
			)
		);

	public static final List<UserDto.QnaResponse> USER_QNA_RESPONSE_LIST =
		new ArrayList<>(
			List.of(
				MY_QNA_RESPONSE, MY_QNA_RESPONSE
			)
		);

	public static final AnswerDto.PageResponse ANSWER_RESPONSE_DTO =
		AnswerDto.PageResponse.builder()
			.answerBody(
				List.of("질문 답변 내용", "질문 답변 내용")
			)
			.build();

	public static final QuestionDto.PageResponse QUESTION_RESPONSE_DTO =
		QuestionDto.PageResponse.builder()
			.questionId(1L)
			.content("질문 내용")
			.answerList(ANSWER_RESPONSE_DTO)
			.build();

	public static final PageImpl<QuestionDto.PageResponse> QUESTION_RESPONSE_PAGE_DTO =
		new PageImpl<QuestionDto.PageResponse>(List.of(QUESTION_RESPONSE_DTO, QUESTION_RESPONSE_DTO));

	public static final FeedDto.PageResponse FEED_PAGE_RESPONSE_DTO =
		FeedDto.PageResponse
			.builder()
			.feedId(1L)
			.category(CategoryType.PLACE.toString())
			.title("피드 제목")
			.body("피드 내용")
			.userId("사용자 번호")
			.nickname("닉네임")
			.viewCount(10L)
			.commentCount(1)
			.likeCount(10L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<FeedDto.PageResponse> FEED_PAGE_RESPONSES =
		new PageImpl<>(List.of(FEED_PAGE_RESPONSE_DTO, FEED_PAGE_RESPONSE_DTO));

	public static final FeedDto.ListResponse USER_FEED_RESPONSE_DTO =
		FeedDto.ListResponse.builder()
			.feedId(1L)
			.title("피드 제목")
			.commentCount(1L)
			.userId("01012345678")
			.nickname("닉네임")
			.hashcode("#123456")
			.category(CategoryType.PLACE.toString())
			.body("피드 내용")
			.likeCount(1L)
			.viewCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<FeedDto.ListResponse> USER_FEED_RESPONSE_PAGE =
		new PageImpl<>(List.of(USER_FEED_RESPONSE_DTO, USER_FEED_RESPONSE_DTO));

	public static final CommentDto.ListResponse USER_COMMENT_RESPONSE_DTO =
		CommentDto.ListResponse.builder()
			.commentId(1L)
			.feedId(1L)
			.nickname("닉네임")
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.body("댓글 내용")
			.likeCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<CommentDto.ListResponse> USER_COMMENT_RESPONSE_PAGE =
		new PageImpl<>(List.of(USER_COMMENT_RESPONSE_DTO, USER_COMMENT_RESPONSE_DTO));

	public static final LoginDto.ResponseDto LOGIN_RESPONSE_DTO =
		LoginDto.ResponseDto.builder()
			.id(1L)
			.userId("01012345678")
			.isFirstLogin(true)
			.nickname("닉네임")
			.level(1)
			.ariFactor(10)
			.role(List.of("ROLE_USER"))
			.profileImage("프로필 이미지")
			.build();
}
