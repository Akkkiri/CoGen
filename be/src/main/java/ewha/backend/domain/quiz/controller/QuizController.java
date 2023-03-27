package ewha.backend.domain.quiz.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.quiz.dto.QuizDto;
import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.domain.quiz.mapper.QuizMapper;
import ewha.backend.domain.quiz.service.QuizService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

	private final QuizMapper quizMapper;
	private final QuizService quizService;

	@PostMapping("add")
	public ResponseEntity<QuizDto.Response> postQuiz(@Valid @RequestBody QuizDto.Post post) {

		Quiz quiz = quizMapper.quizPostToQuiz(post);
		Quiz savedQuiz = quizService.createQuiz(quiz);
		QuizDto.Response response = quizMapper.quizToQuizResponse(savedQuiz);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
