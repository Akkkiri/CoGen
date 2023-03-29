package ewha.backend.domain.quiz.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import ewha.backend.domain.quiz.dto.QuizDto;
import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.global.init.InitConstant;

@Mapper(componentModel = "spring")
public interface QuizMapper {

	Quiz quizPostToQuiz(QuizDto.Post post);

	Quiz quizPatchToQuiz(QuizDto.Patch patch);

	QuizDto.Response quizToQuizResponse(Quiz quiz);

	default List<QuizDto.Response> quizToQuizListResponse(List<Quiz> quizList) {

		if (quizList == null) {
			return null;
		}

		return quizList.stream()
			.map(quiz -> {

				List<String> bodyList = new ArrayList<>(List.of(quiz.getAnswer(), quiz.getDummy1(), quiz.getDummy2()));
				Collections.shuffle(bodyList);

				return QuizDto.Response.builder()
					.content(quiz.getContent())
					.bodies(bodyList)
					.answer(quiz.getAnswer())
					.explanation(quiz.getExplanation())
					.build();
			})
			.collect(Collectors.toList());
	}
}
