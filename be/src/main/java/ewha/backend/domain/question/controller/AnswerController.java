package ewha.backend.domain.question.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.like.service.LikeService;
import ewha.backend.domain.question.dto.AnswerDto;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.mapper.AnswerMapper;
import ewha.backend.domain.question.service.AnswerService;

import ewha.backend.global.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerMapper answerMapper;
	private final AnswerService answerService;
	private final LikeService likeService;

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

	@GetMapping("/questions/{question_id}/answer/list")
	public ResponseEntity<MultiResponseDto<AnswerDto.ListResponse>> getQuestionAnswers(
		@PathVariable("question_id") Long questionId,
		@RequestParam(name = "sort", defaultValue = "new") String sort,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Answer> answerPage = answerService.findQuestionAnswers(questionId, sort, page);
		PageImpl<AnswerDto.ListResponse> responses = answerMapper.answerPageToListResponse(answerPage, likeService);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), answerPage));
	}

	@DeleteMapping("answers/{answer_id}/delete")
	public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("answer_id") Long answerId) {

		answerService.deleteAnswer(answerId);

		return ResponseEntity.noContent().build();
	}
}
