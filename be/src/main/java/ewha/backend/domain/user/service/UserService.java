package ewha.backend.domain.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.security.dto.LoginDto;

public interface UserService {
	User createUser(User user);

	List<List<String>> verifyVerifyDto(UserDto.Verify verifyDto);

	List<String> verifyUserId(String userId);

	List<String> verifyNickname(String nickname);

	List<String> verifyPassword(String password);

	Boolean verifyUserIdForSms(String userId);

	Boolean verifyLoginUserPassword(String password);

	User updateUser(UserDto.UserInfo userInfo);

	void updatePassword(UserDto.Password password);

	User getUser(Long userId);

	User getMyInfo();

	void deleteUser();

	User onFirstLogin(LoginDto.PatchDto patchDto);
	String onFirstLoginQna(List<LoginDto.QnaDto> qnaDtoList);

	Boolean verifyNicknameAndPhoneNumber(String nickname, String phoneNumber);

	Boolean verifyUserIdAndPhoneNumber(String userId, String phoneNumber);

	User findByNickname(String nickname);

	User findByUserId(String userId);

	User findVerifiedUser(Long id);

	User getLoginUserReturnNull();

	User getLoginUser();

	Page<Feed> findUserFeeds(Long userId, int page);

	Page<Feed> findMyBookmark(int page);

	Page<Feed> findMyFeeds(int page);

	Page<Comment> findUserComments(int page);

	Page<Feed> findUserLikedFeed(int page);

	// Page<Question> findUserQuestions(int page);

	void saveUser(User user);
}
