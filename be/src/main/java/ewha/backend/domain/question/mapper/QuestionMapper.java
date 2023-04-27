package ewha.backend.domain.question.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.user.service.UserService;

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
			.content(question.getContent())
			.imagePath(question.getImagePath())
			.thumbnailPath(question.getThumbnailPath())
			.build();
	}

	default PageImpl<QuestionDto.PageResponse> myQuestionsToPageResponse(
		Page<Question> questionPage, UserService userService) {

		if (questionPage == null) {
			return null;
		}

		return new PageImpl<>(questionPage.stream()
			.map(question -> {
				return QuestionDto.PageResponse.builder()
					.questionId(question.getId())
					.content(question.getContent())
					.answerList(
						AnswerDto.PageResponse.builder()
							.answerBody(userService.findMyQuestionAnswer(question)
								.stream()
								.map(Answer::getAnswerBody)
								.collect(Collectors.toList()))
							.build()
					).build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<QuestionDto.PageResponse> userQuestionsToPageResponse(
		Page<Question> questionPage, UserService userService, Long userId) {

		if (questionPage == null) {
			return null;
		}

		return new PageImpl<>(questionPage.stream()
			.map(question -> {
				return QuestionDto.PageResponse.builder()
					.questionId(question.getId())
					.content(question.getContent())
					.answerList(
						AnswerDto.PageResponse.builder()
							.answerBody(userService.findUserQuestionAnswer(userId, question)
								.stream()
								.map(Answer::getAnswerBody)
								.collect(Collectors.toList()))
							.build()
					).build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<QuestionDto.Response> questionsToPageResponse(Page<Question> questionPage) {

		return new PageImpl<>(questionPage.stream()
			.map(question -> {
				return QuestionDto.Response.builder()
					.questionId(question.getId())
					.content(question.getContent())
					.imagePath(question.getImagePath())
					.thumbnailPath(question.getThumbnailPath())
					.build();
			}).collect(Collectors.toList()));
	}
}
