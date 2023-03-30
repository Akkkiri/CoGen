package ewha.backend.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.category.service.CategoryService;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.repository.CommentQueryRepository;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedQueryRepository;
import ewha.backend.domain.qna.entity.Qna;
import ewha.backend.domain.qna.service.QnaService;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.AnswerQueryRepository;
import ewha.backend.domain.question.repository.QuestionQueryRepository;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.entity.UserQna;
import ewha.backend.domain.user.repository.UserQnaRepository;
import ewha.backend.domain.user.repository.UserQueryRepository;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;
import ewha.backend.global.security.dto.LoginDto;
import ewha.backend.global.security.refreshToken.repository.RefreshTokenRepository;
import ewha.backend.global.security.util.CustomAuthorityUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserQueryRepository userQueryRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final QuestionQueryRepository questionQueryRepository;
	private final AnswerQueryRepository answerQueryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final CustomAuthorityUtils customAuthorityUtils;
	private final CategoryService categoryService;
	private final UserQnaRepository userQnaRepository;
	private final QnaService qnaService;

	@Override
	@Transactional
	public User createUser(UserDto.Post postDto) {

		// verifyUserId(postDto.getUserId());
		verifyPassword(postDto.getPassword(), postDto.getPasswordRepeat());

		String createdNickname = createNickname(postDto.getNickname());
		String encryptedPassword = bCryptPasswordEncoder.encode(postDto.getPassword());
		List<String> roles = customAuthorityUtils.createRoles(postDto.getUserId());

		User savedUser = User.builder()
			.userId(postDto.getUserId())
			.password(encryptedPassword)
			.nickname(createdNickname)
			.role(roles)
			.level(1)
			.ariFactor(10)
			.provider("NONE")
			.isFirstLogin(true)
			.build();

		return userRepository.save(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean verifyUserIdForSms(String userId) {
		if (userRepository.existsByUserId(userId))
			return true;
		else
			throw new BusinessLogicException(ExceptionCode.USER_ID_NOT_FOUND);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean verifyLoginUserPassword(String password) {
		User findUser = getLoginUser();
		return findUser.verifyPassword(bCryptPasswordEncoder, password);
	}

	@Override
	@Transactional
	public User updateUser(UserDto.UserInfo userInfo) {

		User findUser = getLoginUser();

		findUser.updateUserInfo(userInfo);

		return userRepository.save(findUser);
	}

	@Override
	@Transactional
	public void updatePassword(UserDto.Password password) {

		User loginUser = getLoginUser();
		User findUser = findByUserId(password.getUserId());

		if (!loginUser.getUserId().equals(findUser.getUserId())) {
			throw new BusinessLogicException(ExceptionCode.USER_ID_NOT_MATCH);
		}

		if (!password.getNewPassword().equals(password.getNewPasswordRepeat())) {
			throw new BusinessLogicException(ExceptionCode.PASSWORDS_NOT_MATCH);
		}

		findUser.updatePassword(bCryptPasswordEncoder.encode(password.getNewPassword()));

		userRepository.save(findUser);

	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(Long userId) {

		// User findUser = getLoginUser();

		return findVerifiedUser(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public User getMyInfo() {

		User findUser = getLoginUser();

		return findUser;
	}

	@Override
	@Transactional
	public void deleteUser() {

		User findUser = getLoginUser();

		userRepository.deleteById(findUser.getId());

	}



	@Override
	@Transactional(readOnly = true)
	public Boolean verifyNicknameAndPhoneNumber(String nickname, String phoneNumber) {

		try {
			userRepository.findByNickname(nickname);
		} catch (RuntimeException e) {
			throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
		}

		User findUser = userRepository.findByNickname(nickname);

		if (findUser.getUserId().equals(phoneNumber)) {
			return true;
		} else {
			throw new BusinessLogicException(ExceptionCode.PHONE_NUMBER_NOT_MATCH);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean verifyUserIdAndPhoneNumber(String userId, String phoneNumber) {

		User findUser = userRepository.findByUserId(userId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		if (findUser.getUserId().equals(phoneNumber)) {
			return true;
		} else {
			throw new BusinessLogicException(ExceptionCode.PHONE_NUMBER_NOT_MATCH);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public User findByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUserId(String userId) {
		Optional<User> optionalUser = userRepository.findByUserId(userId);
		User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public User findVerifiedUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public User getLoginUserReturnNull() {
		Authentication authentication = verifiedAuthentication();

		return userRepository.findByUserId(authentication.getName())
			.orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public User getLoginUser() { // 로그인된 유저 가져오기

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getName() == null || authentication.getName()
			.equals("anonymousUser"))
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);

		Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
		User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feed> findUserFeeds(Long userId, int page) {

		User findUser = findVerifiedUser(userId);

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findFeedPageByUser(findUser, pageRequest);
	}

	/*
	 * 회원가입 처리용 검증, 정보 입력 및 닉네임 난수 생성 코드
	 */

	@Override
	@Transactional(readOnly = true)
	public void verifyUserId(String userId) {
		if (userRepository.existsByUserId(userId)) {
			throw new BusinessLogicException(ExceptionCode.USER_ID_EXISTS);
		}
	}

	@Transactional(readOnly = true)
	private void verifyPassword(String password, String passwordRepeat) {
		if (!password.equals(passwordRepeat)) {
			throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCH);
		}
	}

	@Override
	@Transactional
	public String createNickname(String nickname) {

		Random rand = new Random();
		StringBuilder hashcode = new StringBuilder();
		hashcode.append('#');
		for (int i = 0; i < 6; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			hashcode.append(ran);
		}

		while (userRepository.existsByNickname(nickname + hashcode)) {
			StringBuilder temp = new StringBuilder();
			temp.append('#');
			for (int i = 0; i < 6; i++) {
				String ran = Integer.toString(rand.nextInt(10));
				temp.append(ran);
			}
			hashcode = temp;
		}
		return nickname + hashcode;
	}

	@Override
	@Transactional
	public User onFirstLogin(Long userId, LoginDto.PatchDto patchDto) {

		User findUser = findVerifiedUser(userId);

		findUser.onFirstLogin(patchDto.getGenderType(), patchDto.getAgeType());

		return userRepository.save(findUser);
	}

	@Override
	@Transactional
	public String onFirstLoginQna(Long userId, List<LoginDto.QnaDto> qnaDtoList) {

		User findUser = findVerifiedUser(userId);

		List<LoginDto.QnaDto> filteredList = qnaDtoList.stream()
			.filter(qnaDto -> !qnaDto.getAnswerBody().isBlank())
			.collect(Collectors.toList());

		if (filteredList.size() < 3) {
			throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_QNA);
		}

		filteredList
			.forEach(qnaDto -> {
				Qna qna = qnaService.findVerifiedQna(qnaDto.getQnaId());
				UserQna userQna = UserQna.builder()
					.answerBody(qnaDto.getAnswerBody())
					.user(findUser)
					.qna(qna)
					.build();
				userQnaRepository.save(userQna);
			});

		findUser.setIsFirstLogin(false);

		userRepository.save(findUser);

		return "QnA Inserted.";
	}

	/*
	 * 마이 페이지에서 표시될 정보. 나의 게시글, 댓글, 북마크, 대답한 질문
	 */

	@Override
	@Transactional(readOnly = true)
	public Page<Question> findMyQuestions(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return questionQueryRepository.findMyQuestions(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Answer> findMyQuestionAnswer(Question question) {

		User findUser = getLoginUser();

		return answerQueryRepository.findMyQuestionAnswer(findUser, question);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feed> findMyBookmark(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findMyBookmarks(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feed> findMyFeeds(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findFeedPageByUser(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Question> findUserQuestions(Long userId, int page) {

		User findUser = findVerifiedUser(userId);

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return questionQueryRepository.findUserQuestions(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Answer> findUserQuestionAnswer(Long userId, Question question) {

		return answerQueryRepository.findUserQuestionAnswer(userId, question);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Comment> findUserComments(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return commentQueryRepository.findCommentListByUser(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Feed> findUserLikedFeed(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findLikedFeedListByUser(findUser, pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public void saveUser(User user) {
		userRepository.save(user);
	}

	private Authentication verifiedAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
		return authentication;
	}
}
