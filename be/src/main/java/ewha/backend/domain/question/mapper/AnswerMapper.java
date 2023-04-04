package ewha.backend.domain.question.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.entity.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

	Answer postAnswerToAnswer(AnswerDto.Post post);

	Answer patchAnswerToAnswer(AnswerDto.Patch patch);

	default AnswerDto.Response answerToAnswerResponse(Answer answer) {

		return AnswerDto.Response.builder()
			.answerId(answer.getId())
			.answerBody(answer.getAnswerBody())
			.build();
	}

	default PageImpl<AnswerDto.ListResponse> answerPageToListResponse(Page<Answer> answerPage) {

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
				listResponseBuilder.nickname(nickPre);
				listResponseBuilder.hashcode(nickSuf);
				listResponseBuilder.profileImage(answer.getUser().getProfileImage());
				listResponseBuilder.thumbnailPath(answer.getUser().getThumbnailPath());
				listResponseBuilder.answerBody(answer.getAnswerBody());
				listResponseBuilder.likeCount(answer.getLikeCount());
				listResponseBuilder.reportCount(answer.getReportCount());
				listResponseBuilder.createdAt(answer.getCreatedAt());
				listResponseBuilder.modifiedAt(answer.getModifiedAt());

				return listResponseBuilder.build();
			}).collect(Collectors.toList()));

	}
}
