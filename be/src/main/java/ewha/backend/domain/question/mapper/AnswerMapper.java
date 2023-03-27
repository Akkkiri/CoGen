package ewha.backend.domain.question.mapper;

import org.mapstruct.Mapper;

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
}
