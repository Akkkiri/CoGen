package ewha.backend.domain.question.mapper;

import org.mapstruct.Mapper;

import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

	Question questionPostToQuestion(QuestionDto.Post postQuestion);

	Question questionPatchToQuestion(QuestionDto.Patch patchQuestion);

	default QuestionDto.Response questionToQuestionResponse(Question question) {

		if (question == null) {
			return null;
		}

		return QuestionDto.Response.builder()
			.questionId(question.getId())
			.title(question.getTitle())
			.content(question.getContent())
			.imagePath(question.getImagePath())
			.thumbnailPath(question.getThumbnailPath())
			.build();
	}
}
