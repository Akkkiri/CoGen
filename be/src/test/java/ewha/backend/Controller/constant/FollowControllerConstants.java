package ewha.backend.Controller.constant;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.follow.dto.FollowDto;

public class FollowControllerConstants {

	public static final FollowDto.FollowerResponse FOLLOWER_RESPONSE_DTO =
		FollowDto.FollowerResponse.builder()
			.userId(1L)
			.nickname("닉네임")
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.isFollowing(false)
			.build();

	public static final PageImpl<FollowDto.FollowerResponse> FOLLOWER_RESPONSE_PAGE =
		new PageImpl<>(List.of(FOLLOWER_RESPONSE_DTO, FOLLOWER_RESPONSE_DTO));

	public static final FollowDto.FollowingResponse FOLLOWING_RESPONSE_DTO =
		FollowDto.FollowingResponse.builder()
			.userId(1L)
			.nickname("닉네임")
			.profileImage("프로필 사진")
			.thumbnailPath("썸네일 경로")
			.isFollowing(true)
			.build();

	public static final PageImpl<FollowDto.FollowingResponse> FOLLOWING_RESPONSE_PAGE =
		new PageImpl<>(List.of(FOLLOWING_RESPONSE_DTO, FOLLOWING_RESPONSE_DTO));

}
