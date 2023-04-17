package ewha.backend.domain.question.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

	Answer postAnswerToAnswer(AnswerDto.Post post);

	Answer patchAnswerToAnswer(AnswerDto.Patch patch);

	default AnswerDto.Response answerToAnswerResponse(Answer answer) {

		User user = answer.getUser();

		String[] nick = user.getNickname().split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		AnswerDto.Response.ResponseBuilder responseBuilder = AnswerDto.Response.builder();

		responseBuilder.answerId(answer.getId());
		responseBuilder.userId(answer.getUser().getId());
		responseBuilder.nickname(nickPre);
		responseBuilder.hashcode(nickSuf);
		responseBuilder.profileImage(answer.getUser().getProfileImage());
		responseBuilder.thumbnailPath(answer.getUser().getThumbnailPath());
		responseBuilder.answerBody(answer.getAnswerBody());
		responseBuilder.likeCount(answer.getLikeCount());
		responseBuilder.reportCount(answer.getReportCount());
		responseBuilder.createdAt(answer.getCreatedAt());
		responseBuilder.modifiedAt(answer.getModifiedAt());

		return responseBuilder.build();
	}

	default PageImpl<AnswerDto.ListResponse> answerPageToListResponse(
		Page<Answer> answerPage, LikeService likeService) {

		if (answerPage == null) {
			return null;
		}

		return new PageImpl<>(answerPage.stream()
			.map(answer -> {
				AnswerDto.ListResponse.ListResponseBuilder listResponseBuilder = AnswerDto.ListResponse.builder();

				String[] nick = answer.getUser().getNickname().split("#");
				String nickPre = nick[0];
				String nickSuf = "#" + nick[1];

				listResponseBuilder.answerId(answer.getId());
				listResponseBuilder.userId(answer.getUser().getId());
				listResponseBuilder.nickname(nickPre);
				listResponseBuilder.hashcode(nickSuf);
				listResponseBuilder.profileImage(answer.getUser().getProfileImage());
				listResponseBuilder.thumbnailPath(answer.getUser().getThumbnailPath());
				listResponseBuilder.answerBody(answer.getAnswerBody());
				listResponseBuilder.isLiked(likeService.isLikedAnswer(answer));
				listResponseBuilder.likeCount(answer.getLikeCount());
				listResponseBuilder.reportCount(answer.getReportCount());
				listResponseBuilder.createdAt(answer.getCreatedAt());
				listResponseBuilder.modifiedAt(answer.getModifiedAt());

				return listResponseBuilder.build();
			}).collect(Collectors.toList()));

	}
}
