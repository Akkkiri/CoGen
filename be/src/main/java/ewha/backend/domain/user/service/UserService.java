package ewha.backend.domain.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.security.dto.LoginDto;

public interface UserService {
	User createUser(UserDto.Post postDto);

	Boolean verifyUserIdForSms(String userId);

	Boolean verifyLoginUserPassword(String password);

	User updateUser(UserDto.UserInfo userInfo);

	void updatePassword(UserDto.Password password);

	public void updatePasswordWithSms(UserDto.Password password);

	User getUser(Long userId);

	User getMyInfo();

	void deleteUser();

	User onFirstLogin(Long userId, LoginDto.PatchDto patchDto);

	String onFirstLoginQna(Long userId, List<LoginDto.QnaDto> qnaDtoList);
	String updateMyQna(List<LoginDto.QnaDto> qnaDtoList);

	Boolean verifyNicknameAndPhoneNumber(String nickname, String phoneNumber);

	void verifyUserIdAndPhoneNumber(String userId);

	User findByNickname(String nickname);

	User findByUserId(String userId);

	User findVerifiedUser(Long id);

	User getLoginUserReturnNull();

	User getLoginUser();

	Page<Feed> findUserFeeds(Long userId, int page);

	Page<Question> findMyQuestions(int page);

	List<Answer> findMyQuestionAnswer(Question question);

	Page<Feed> findMyBookmark(int page);

	Page<Feed> findMyFeeds(int page);

	Page<Question> findUserQuestions(Long userId, int page);

	List<Answer> findUserQuestionAnswer(Long userId, Question question);

	Page<Comment> findUserComments(int page);

	Page<Feed> findUserLikedFeed(int page);

	// Page<Question> findUserQuestions(int page);

	void saveUser(User user);

	String createNickname(String nickname);

	void verifyUserId(String userId);
}
