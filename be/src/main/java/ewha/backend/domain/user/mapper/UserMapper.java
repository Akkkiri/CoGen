package ewha.backend.domain.user.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import ewha.backend.domain.qna.entity.Qna;
import ewha.backend.domain.qna.service.QnaService;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.security.dto.LoginDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
	default User userPostToUser(UserDto.Post postDto) {
		return User.builder()
			.userId(postDto.getUserId())
			.nickname(postDto.getNickname())
			.password(postDto.getPassword())
			.build();
	}

	User userInfotoUser(UserDto.UserInfo userInfo);

	default UserDto.Response userToUserResponse(User user) {

		UserDto.Response.ResponseBuilder responseBuilder = UserDto.Response.builder();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		responseBuilder.id(user.getId());
		responseBuilder.userId(user.getUserId());
		responseBuilder.nickname(nickPre);
		responseBuilder.hashcode(nickSuf);
		responseBuilder.genderType(user.getGenderType());
		responseBuilder.ageType(user.getAgeType());
		responseBuilder.ariFactor(user.getAriFactor());
		// responseBuilder.qnas(user.getQnas());
		responseBuilder.level(user.getLevel());
		responseBuilder.role(user.getRole());
		responseBuilder.profileImage(user.getProfileImage());
		responseBuilder.thumbnailPath(user.getThumbnailPath());

		return responseBuilder.build();
	}

	UserDto.PostResponse userToUserPostResponse(User user);

	default List<UserDto.VerifyResponse> listToVerifyResponse(List<List<String>> list) {

		List<UserDto.VerifyResponse> responses = new ArrayList<>();

		list.forEach(stringList -> {
			UserDto.VerifyResponse.VerifyResponseBuilder verifyResponse = UserDto.VerifyResponse.builder();

			verifyResponse.status(stringList.get(0));
			verifyResponse.message(stringList.get(1));
			verifyResponse.field(stringList.get(2));
			verifyResponse.rejectedValue(stringList.get(3));
			verifyResponse.reason(stringList.get(4));

			responses.add(verifyResponse.build());
		});

		return responses;
	}

	default UserDto.MyPageResponse userToMyPageResponse(User user) {

		if (user == null) {
			return null;
		}

		UserDto.MyPageResponse.MyPageResponseBuilder myPageResponseBuilder = UserDto.MyPageResponse.builder();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		myPageResponseBuilder.userId(user.getUserId());
		myPageResponseBuilder.nickname(nickPre);
		myPageResponseBuilder.hashcode(nickSuf);
		myPageResponseBuilder.level(user.getLevel());
		myPageResponseBuilder.ariFactor(user.getAriFactor());
		myPageResponseBuilder.friendsNum(user.getFollowingCount());
		myPageResponseBuilder.profileImage(user.getProfileImage());
		myPageResponseBuilder.thumbnailPath(user.getThumbnailPath());

		return myPageResponseBuilder.build();
	}

	default UserDto.UserPageResponse userToUserPageResponse(User user) {

		if (user == null) {
			return null;
		}

		UserDto.UserPageResponse.UserPageResponseBuilder userPageResponseBuilder = UserDto.UserPageResponse.builder();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		userPageResponseBuilder.userId(user.getUserId());
		userPageResponseBuilder.nickname(nickPre);
		userPageResponseBuilder.hashcode(nickSuf);
		userPageResponseBuilder.level(user.getLevel());
		userPageResponseBuilder.ariFactor(user.getAriFactor());
		userPageResponseBuilder.friendsNum(user.getFollowingCount());
		userPageResponseBuilder.profileImage(user.getProfileImage());
		userPageResponseBuilder.thumbnailPath(user.getThumbnailPath());

		return userPageResponseBuilder.build();
	}

	LoginDto.ResponseDto userToLoginResponse(User user);

	default List<UserDto.QnaResponse> userToQnaResponse(User user, QnaService qnaService) {

		return user.getUserQnas().stream()
			.map(userQna -> {
				Qna qna = qnaService.findVerifiedQna(userQna.getQna().getId());

					return UserDto.QnaResponse.builder()
						.qnaId(qna.getId())
						.content(qna.getContent())
						.answerBody(userQna.getAnswerBody())
						.build();

			}).collect(Collectors.toList());
	}
}
