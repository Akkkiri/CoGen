package ewha.backend.domain.quiz.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

	private final QuizMapper quizMapper;
	private final QuizService quizService;

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> postQuiz(@Valid @RequestBody QuizDto.Post post) {

		Quiz quiz = quizMapper.quizPostToQuiz(post);
		Quiz savedQuiz = quizService.createQuiz(quiz);
		// QuizDto.Response response = quizMapper.quizToQuizResponse(savedQuiz);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{quiz_id}/edit")
	public ResponseEntity<HttpStatus> patchQuiz(
		@PathVariable("quiz_id") Long quizId,
		@Valid @RequestBody QuizDto.Patch patch) {

		Quiz quiz = quizMapper.quizPatchToQuiz(patch);
		Quiz updatedQuiz = quizService.updateQuiz(quizId, quiz);
		// QuizDto.Response response = quizMapper.quizToQuizResponse(updatedQuiz);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/weekly")
	public ResponseEntity<List<QuizDto.Response>> getWeeklyQuiz() {

		List<Quiz> quizList = quizService.findWeeklyQuizzes();
		List<QuizDto.Response> responses = quizMapper.quizToQuizListResponse(quizList);

		return ResponseEntity.ok().body(responses);
	}

	@PostMapping("/weekly/clear")
	public String clearWeeklyQuiz() {
		return quizService.clearWeeklyQuizzes();
	}

	@DeleteMapping("/{quiz_id}/delete")
	public ResponseEntity<String> deleteQuiz(@PathVariable("quiz_id") Long quizId) {

		quizService.deleteQuiz(quizId);

		return ResponseEntity.noContent().build();
	}
}
