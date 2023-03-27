package ewha.backend.domain.quiz.mapper;

import org.mapstruct.Mapper;

import ewha.backend.domain.quiz.dto.QuizDto;
import ewha.backend.domain.quiz.entity.Quiz;

@Mapper(componentModel = "spring")
public interface QuizMapper {

	Quiz quizPostToQuiz(QuizDto.Post post);
	Quiz quizPatchToQuiz(QuizDto.Patch patch);
	QuizDto.Response quizToQuizResponse(Quiz quiz);
}
