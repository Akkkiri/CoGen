package ewha.backend.domain.user.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.mapper.CommentMapper;
import ewha.backend.domain.feed.dto.FeedDto;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.follow.repository.FollowQueryRepository;
import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.qna.service.QnaService;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.mapper.QuestionMapper;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.dto.MultiResponseDto;
import ewha.backend.global.security.dto.LoginDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserMapper userMapper;
	private final UserService userService;
	private final FeedMapper feedMapper;
	private final CommentMapper commentMapper;
	private final QuestionMapper questionMapper;
	private final AwsS3Service awsS3Service;
	private final QnaService qnaService;
	private final LikeService likeService;
	private final FollowQueryRepository followQueryRepository;

	@GetMapping("/oauth/signin")
	@ResponseBody
	public String oauthLoginInfo(Authentication authentication,
		@AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println(attributes);
		// PrincipalOauth2UserService의 getAttributes내용과 같음

		Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
		// attributes == attributes1

		return attributes.toString();     //세션에 담긴 user 가져올 수 있음
	}

	@PostMapping("/users/signup")
	public ResponseEntity<UserDto.PostResponse> postUser(@Valid @RequestBody UserDto.Post postDto) {

		User createdUser = userService.createUser(postDto);
		UserDto.PostResponse response = userMapper.userToUserPostResponse(createdUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/users/{user_id}/firstlogin")
	public ResponseEntity<UserDto.Response> firstLoginUser(
		@PathVariable("user_id") Long userId,
		@Valid @RequestBody LoginDto.PatchDto firstPatchDto) {

		User firstLoginUser = userService.onFirstLogin(userId, firstPatchDto);
		UserDto.Response response = userMapper.userToUserResponse(firstLoginUser);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/users/{user_id}/firstqna")
	public ResponseEntity<String> firstLoginQna(
		@PathVariable("user_id") Long userId,
		@Valid @RequestBody List<LoginDto.QnaDto> qnaDtoList) {

		String response = userService.onFirstLoginQna(userId, qnaDtoList);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/mypage/patchqna")
	public ResponseEntity<String> updateQna(
		@Valid @RequestBody List<LoginDto.QnaDto> qnaDtoList) {

		String response = userService.updateMyQna(qnaDtoList);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/mypage/patch")
	public ResponseEntity<UserDto.Response> patchUser(
		@Valid @RequestBody UserDto.UserInfo userInfo) throws Exception {

		User updatedUser = userService.updateUser(userInfo);

		UserDto.Response response = userMapper.userToUserResponse(updatedUser);

		return ResponseEntity.ok().body(response);
	}

	// @PostMapping("/mypage/patch")
	// public ResponseEntity<UserDto.Response> patchUser(
	// 	@RequestParam(value = "image") @Nullable MultipartFile multipartFile,
	// 	@Valid @RequestPart(value = "patch") UserDto.UserInfo userInfo) throws Exception {
	//
	// 	List<String> imagePath = null;
	//
	// 	User loginUser = userService.getLoginUser();
	// 	User updatedUser = userService.updateUser(userInfo);
	//
	// 	if (loginUser.getProfileImage() != null && userInfo.getProfileImage() != null && multipartFile == null
	// 		&& userInfo.getProfileImage().equals(loginUser.getProfileImage())) {
	// 		updatedUser.setProfileImage(updatedUser.getProfileImage());
	// 		updatedUser.setThumbnailPath(updatedUser.getThumbnailPath());
	// 	} else if (loginUser.getProfileImage() != null && userInfo.getProfileImage() == null && multipartFile != null) {
	// 		imagePath = awsS3Service.updateORDeleteFeedImageFromS3(updatedUser.getId(), multipartFile);
	// 		updatedUser.setProfileImage(imagePath.get(0));
	// 		updatedUser.setThumbnailPath(imagePath.get(1));
	// 	} else if (loginUser.getProfileImage() == null && userInfo.getProfileImage() == null && multipartFile != null) {
	// 		imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedUser.getId());
	// 		updatedUser.setProfileImage(imagePath.get(0));
	// 		updatedUser.setThumbnailPath(imagePath.get(1));
	// 	} else if (loginUser.getProfileImage() != null && multipartFile == null && userInfo.getProfileImage() == null) {
	// 		awsS3Service.updateORDeleteUserImageFromS3(updatedUser.getId(), multipartFile);
	// 		updatedUser.setProfileImage(null);
	// 		updatedUser.setThumbnailPath(null);
	// 	}
	//
	// 	userService.saveUser(updatedUser);
	//
	// 	UserDto.Response response = userMapper.userToUserResponse(updatedUser);
	//
	// 	return ResponseEntity.ok().body(response);
	// }

	@PatchMapping("/mypage/patch/password")
	public ResponseEntity<HttpStatus> patchPassword(@Valid @RequestBody UserDto.Password password) {

		userService.updatePassword(password);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/mypage/signout")
	public ResponseEntity deleteUser() {

		userService.deleteUser();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * 마이 페이지에서 표시될 정보. 나의 게시글, 댓글, 북마크, 대답한 질문
	 */

	@GetMapping("/mypage")
	public ResponseEntity<UserDto.MyPageResponse> getMyPage() {

		User findUser = userService.getMyInfo();
		UserDto.MyPageResponse response = userMapper.userToMyPageResponse(findUser);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/mypage/myqna")
	public ResponseEntity<List<UserDto.QnaResponse>> getMyQna() {

		User findUser = userService.getLoginUser();
		List<UserDto.QnaResponse> responses = userMapper.userToQnaResponse(findUser, qnaService);

		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/mypage/myquestions")
	public ResponseEntity<MultiResponseDto<QuestionDto.PageResponse>> getMyQuestions(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Question> questionPage = userService.findMyQuestions(page);
		PageImpl<QuestionDto.PageResponse> responses =
			questionMapper.myQuestionsToPageResponse(questionPage, userService);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), questionPage));
	}

	@GetMapping("/mypage/bookmarks")
	public ResponseEntity<MultiResponseDto<FeedDto.PageResponse>> getMyBookmarks(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedPage = userService.findMyBookmark(page);
		PageImpl<FeedDto.PageResponse> responses = feedMapper.myBookmarksToPageResponse(feedPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedPage));
	}

	@GetMapping("/mypage/myfeeds")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getMyFeeds(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedPage = userService.findMyFeeds(page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.myFeedsToPageResponse(feedPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedPage));
	}

	@GetMapping("/mypage/mycomments")
	public ResponseEntity<MultiResponseDto<CommentDto.ListResponse>> getMyComments(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Comment> commentList = userService.findUserComments(page);
		PageImpl<CommentDto.ListResponse> responses = commentMapper.myCommentsToPageResponse(commentList, likeService);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), commentList));
	}

	@GetMapping("/mypage/mylikedfeeds")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getMyLikedFeed(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = userService.findUserLikedFeed(page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.myFeedsToPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
	}

	/*
	 * 다른 사용자 페이지 조회
	 */

	@GetMapping("/users/{user_id}")
	public ResponseEntity<UserDto.UserPageResponse> getUserPage(@PathVariable("user_id") Long userId) {

		User findUser = userService.getUser(userId);
		User loginUser = userService.getLoginUserReturnNull();
		UserDto.UserPageResponse response;

		if (loginUser != null && followQueryRepository.existByFollowingUserAndFollowedUser(loginUser, findUser)) {
			response = userMapper.userToUserPageResponse(findUser, true);
		} else {
			response = userMapper.userToUserPageResponse(findUser, false);
		}

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/users/{user_id}/qna")
	public ResponseEntity<List<UserDto.QnaResponse>> getUserQna(@PathVariable("user_id") Long userId) {

		User findUser = userService.getUser(userId);
		List<UserDto.QnaResponse> responses = userMapper.userToQnaResponse(findUser, qnaService);

		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/users/{user_id}/questions")
	public ResponseEntity<MultiResponseDto<QuestionDto.PageResponse>> getUserQuestions(
		@PathVariable("user_id") Long userId,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Question> questionPage = userService.findUserQuestions(userId, page);
		PageImpl<QuestionDto.PageResponse> responses =
			questionMapper.userQuestionsToPageResponse(questionPage, userService, userId);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), questionPage));
	}

	@GetMapping("/users/{user_id}/feeds")
	public ResponseEntity<MultiResponseDto<FeedDto.PageResponse>> getUserFeeds(
		@PathVariable("user_id") Long userId,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedPage = userService.findUserFeeds(userId, page);
		PageImpl<FeedDto.PageResponse> responses = feedMapper.userFeedsToPageResponse(feedPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedPage));
	}
}
