package ewha.backend.domain.comment.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.comment.dto.CommentDto;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	Comment commentPostToComment(CommentDto.Post postComment);

	Comment commentPatchToComment(CommentDto.Patch patchComment);

	default CommentDto.Response commentToCommentResponse(Comment comment) {

		User user = comment.getUser();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		return CommentDto.Response.builder()
			.commentId(comment.getId())
			.feedId(comment.getFeed().getId())
			.userInfo(UserDto.BasicResponse.builder()
				.id(user.getId())
				.userId(user.getUserId())
				.nickname(nickPre)
				.hashcode(nickSuf)
				.level(user.getLevel())
				.profileImage(user.getProfileImage())
				.thumbnailPath(user.getThumbnailPath())
				.build())
			.body(comment.getBody())
			.likeCount(comment.getLikeCount())
			.createdAt(comment.getCreatedAt())
			.modifiedAt(comment.getModifiedAt())
			.build();
	}

	default CommentDto.GetResponse commentToCommentGetResponse(Comment comment, Boolean isLiked) {

		User user = comment.getUser();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		return CommentDto.GetResponse.builder()
			.commentId(comment.getId())
			.feedId(comment.getFeed().getId())
			.userInfo(UserDto.BasicResponse.builder()
				.id(user.getId())
				.userId(user.getUserId())
				.nickname(nickPre)
				.hashcode(nickSuf)
				.level(user.getLevel())
				.profileImage(user.getProfileImage())
				.thumbnailPath(user.getThumbnailPath())
				.build())
			.body(comment.getBody())
			.isLiked(isLiked)
			.likeCount(comment.getLikeCount())
			.reportCount(comment.getReportCount())
			.createdAt(comment.getCreatedAt())
			.modifiedAt(comment.getModifiedAt())
			.build();
	}

	default PageImpl<CommentDto.ListResponse> myCommentsToPageResponse(Page<Comment> commentList) {

		if (commentList == null) {
			return null;
		}

		return new PageImpl<>(commentList.stream()
			.map(comment -> {

				String[] nick = comment.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				return CommentDto.ListResponse.builder()
					.feedId(comment.getFeed().getId())
					.commentId(comment.getId())
					.nickname(nickPre)
					.profileImage(comment.getUser().getProfileImage())
					.thumbnailPath(comment.getUser().getThumbnailPath())
					.body(comment.getBody())
					.likeCount(comment.getLikeCount())
					.createdAt(comment.getCreatedAt())
					.modifiedAt(comment.getModifiedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<CommentDto.Response> feedCommentsToPageResponse(Page<Comment> commentList) {

		if (commentList == null) {
			return null;
		}

		return new PageImpl<>(commentList.stream()
			.map(this::commentToCommentResponse)
			.collect(Collectors.toList()));
	}

	default PageImpl<CommentDto.GetResponse> getFeedCommentsToPageResponse(
		Page<Comment> commentList, LikeService likeService) {

		if (commentList == null) {
			return null;
		}

		return new PageImpl<>(commentList.stream()
			.map(comment -> commentToCommentGetResponse(comment, likeService.isLikedComment(comment)))
			.collect(Collectors.toList()));
	}
}
