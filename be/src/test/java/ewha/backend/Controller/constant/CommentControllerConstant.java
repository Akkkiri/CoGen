package ewha.backend.Controller.constant;

import static ewha.backend.Controller.constant.FeedControllerConstant.*;
import static ewha.backend.Controller.constant.UserControllerConstant.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.user.dto.UserDto;

public class CommentControllerConstant {

	public static final CommentDto.Post POST_COMMENT_DTO =
		CommentDto.Post.builder()
			.body("댓글 내용")
			.build();

	public static final CommentDto.Response COMMENT_RESPONSE_DTO =
		CommentDto.Response.builder()
			.commentId(1L)
			.feedId(1L)
			.userInfo(USER_BASIC_RESPONSE)
			.body("댓글 내용")
			.likeCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final CommentDto.Patch PATCH_COMMENT_DTO =
		CommentDto.Patch.builder()
			.body("댓글 내용")
			.build();

	public static final PageImpl<CommentDto.Response> COMMENT_RESPONSE_PAGE =
		new PageImpl<>(List.of(COMMENT_RESPONSE_DTO, COMMENT_RESPONSE_DTO));

	public static final CommentDto.GetResponse GET_COMMENT_RESPONSE_DTO =
		CommentDto.GetResponse.builder()
			.commentId(1L)
			.feedId(1L)
			.userInfo(USER_BASIC_RESPONSE)
			.body("댓글 내용")
			.isLiked(true)
			.likeCount(1L)
			.reportCount(0L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<CommentDto.GetResponse> GET_COMMENT_RESPONSE_PAGE =
		new PageImpl<>(List.of(GET_COMMENT_RESPONSE_DTO, GET_COMMENT_RESPONSE_DTO));
}
