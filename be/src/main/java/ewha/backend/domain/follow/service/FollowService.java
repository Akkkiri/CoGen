package ewha.backend.domain.follow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.follow.entity.Follow;
import ewha.backend.domain.follow.repository.FollowQueryRepository;
import ewha.backend.domain.follow.repository.FollowRepository;
import ewha.backend.domain.notification.entity.NotificationType;
import ewha.backend.domain.notification.service.NotificationService;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final FollowQueryRepository followQueryRepository;
	private final UserService userService;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	public String createOrDeleteFollow(Long followedUserId) {

		User followingUser = userService.getLoginUser();
		Long followingUserId = followingUser.getId();

		User followedUser = userService.findVerifiedUser(followedUserId);

		if (followingUser.equals(followedUser)) {
			throw new BusinessLogicException(ExceptionCode.CANNOT_FOLLOW_MYSELF);
		} else {
			if (followQueryRepository.findFollowByUserIds(followingUserId, followedUserId) == null) {

				Follow createdFollow = Follow.builder()
					.followingUser(followingUser)
					.followedUser(followedUser)
					.build();

				followRepository.save(createdFollow);

				followingUser.addFollowing();
				followedUser.addFollower();

				if (followingUser.getFollowingCount() >= 10 && !followingUser.getHasTenFollowing()) {
					followingUser.addAriFactor(10);
					followingUser.setHasTenFollowing(true);
				}

				if (followingUser.getFollowingCount() >= 30 && !followingUser.getHasThirtyFollowing()) {
					followingUser.addAriFactor(15);
					followingUser.setHasThirtyFollowing(true);
				}

				if (followingUser.getFollowingCount() >= 50 && !followingUser.getHasFiftyFollowing()) {
					followingUser.addAriFactor(20);
					followingUser.setHasFiftyFollowing(true);
				}

				if (followingUser.getAriFactor() >= 50) {
					followingUser.addLevel(followingUser.getAriFactor());
				}

				userRepository.save(followingUser);

				String body = followingUser.getNickname() + "님이 회원님을 팔로우하기 시작했습니다.";
				String url = "/user/" + followingUser.getId();
				notificationService.send(followedUser, url, body, null, NotificationType.FOLLOW);

				return "Create Follow";

			} else {

				Follow findFollow = followQueryRepository.findFollowByUserIds(followingUserId, followedUserId);

				followRepository.delete(findFollow);

				followingUser.removeFollowing();
				followedUser.removeFollower();

				return "Delete Follow";
			}
		}
	}

	public Page<User> findFollowers(Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowersByUserId(userId, pageRequest);
	}

	public Page<User> findFollowersWithLoginUser(Long loginUserId, Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowersWithLoginUserByUserId(loginUserId, userId, pageRequest);
	}

	public List<User> findFollowingsList(Long followingUserId, Page<User> userPage) {
		return userPage.stream()
			.filter(user -> followQueryRepository.findFollowByUserIds(followingUserId, user.getId()) != null)
			.collect(Collectors.toList());
	}

	public Page<User> findFollowings(Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowingsByUserId(userId, pageRequest);
	}

	public Boolean isFollowing(Long followingUserId, Long followedUserId) {
		return followQueryRepository.findFollowByUserIds(followingUserId, followedUserId) != null;
	}
}
