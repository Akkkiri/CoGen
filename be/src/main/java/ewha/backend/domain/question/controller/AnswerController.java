package ewha.backend.domain.question.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.mapper.AnswerMapper;
import ewha.backend.domain.question.service.AnswerService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerMapper answerMapper;
	private final AnswerService answerService;

	@PostMapping("/questions/{question_id}/answer/add")
	public ResponseEntity<AnswerDto.Response> postAnswer(
		@PathVariable("question_id") Long questionId,
		@Valid @RequestBody AnswerDto.Post post) {

		Answer answer = answerMapper.postAnswerToAnswer(post);
		Answer savedAnswer = answerService.postAnswer(answer, questionId);
		AnswerDto.Response response = answerMapper.answerToAnswerResponse(savedAnswer);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/answers/{answer_id}/edit")
	public ResponseEntity<AnswerDto.Response> patchAnswer(
		@PathVariable("answer_id") Long answerId,
		@Valid @RequestBody AnswerDto.Patch patch) {

		Answer answer = answerMapper.patchAnswerToAnswer(patch);
		Answer patchedAnswer = answerService.patchAnswer(answer, answerId);
		AnswerDto.Response response = answerMapper.answerToAnswerResponse(patchedAnswer);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("answers/{answer_id}/delete")
	public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("answer_id") Long answerId) {

		answerService.deleteAnswer(answerId);

		return ResponseEntity.noContent().build();
	}
}
