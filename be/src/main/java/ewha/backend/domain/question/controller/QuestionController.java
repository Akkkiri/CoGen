package ewha.backend.domain.question.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ewha.backend.domain.image.service.AwsS3Service;
import ewha.backend.domain.question.dto.QuestionDto;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.mapper.QuestionMapper;
import ewha.backend.domain.question.service.QuestionService;
import ewha.backend.global.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;
	private final QuestionMapper questionMapper;
	private final AwsS3Service awsS3Service;

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> postQuestion(
		@Nullable @RequestParam(value = "image", required = false) MultipartFile multipartFile,
		@Valid @RequestPart(value = "post") QuestionDto.Post postQuestion) throws Exception {

		List<String> imagePath = null;

		Question question = questionMapper.questionPostToQuestion(postQuestion);
		Question createdQuestion = questionService.createQuestion(question);

		if (multipartFile != null) {
			imagePath = awsS3Service.uploadQuestionImageToS3(multipartFile, createdQuestion.getId());
			if (imagePath.size() != 0) {
				createdQuestion.addImagePaths(imagePath.get(0), imagePath.get(1));
			}
		}

		// QuestionDto.Response response = questionMapper.questionToQuestionResponse(createdQuestion);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{question_id}/edit")
	public ResponseEntity<String> patchQuestion(@PathVariable("question_id") Long questionId,
		@Nullable @RequestParam(value = "image", required = false) MultipartFile multipartFile,
		@Valid @RequestPart(value = "patch") QuestionDto.Patch patchQuestion) throws Exception {

		List<String> imagePath = null;

		Question findQuestion = questionService.findVerifiedQuestion(questionId);
		Question question = questionMapper.questionPatchToQuestion(patchQuestion);
		Question updatedQuestion = questionService.updateQuestion(question, questionId);

		// MultipartFile이 없으면서, 기존 피드에 이미지가 있고, 요청 JSON에도 이미지가 있고, 두 경로가 같은 경우
		if (multipartFile == null && findQuestion.getImagePath() != null && patchQuestion.getImagePath() != null
			&& patchQuestion.getImagePath().equals(updatedQuestion.getImagePath())) {
			updatedQuestion.addImagePaths(updatedQuestion.getImagePath(), updatedQuestion.getThumbnailPath());
			// 기존 피드에 이미지가 있고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
		} else if (findQuestion.getImagePath() != null && patchQuestion.getImagePath() == null
			&& multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteFeedImageFromS3(updatedQuestion.getId(), multipartFile);
			updatedQuestion.addImagePaths(imagePath.get(0), imagePath.get(1));
			// 기존 피드에 이미지가 없고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
		} else if (findQuestion.getImagePath() == null && patchQuestion.getImagePath() == null
			&& multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedQuestion.getId());
			updatedQuestion.addImagePaths(imagePath.get(0), imagePath.get(1));
			// 기존 피드에 이미지가 있으면서 요청 JSON에 이미지가 없고, multipartFile도 없는 경우
		} else if (findQuestion.getImagePath() != null && patchQuestion.getImagePath() == null
			&& multipartFile == null) {
			awsS3Service.updateORDeleteFeedImageFromS3(updatedQuestion.getId(), multipartFile);
			updatedQuestion.addImagePaths(null, null);
		}

		questionService.saveQuestion(updatedQuestion);

		QuestionDto.Response response = questionMapper.questionToQuestionResponse(updatedQuestion);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/weekly")
	public ResponseEntity<QuestionDto.Response> getQuestion() {

		Question question = questionService.getQuestion();
		QuestionDto.Response response = questionMapper.questionToQuestionResponse(question);

		return ResponseEntity.ok().body(response);
	}

	// @GetMapping("/{question_id}")
	// public ResponseEntity<QuestionDto.Response> getQuestion(@PathVariable("question_id") Long questionId) {
	//
	// 	Question question = questionService.getQuestion(questionId);
	// 	QuestionDto.Response response = questionMapper.questionToQuestionResponse(question);
	//
	// 	return ResponseEntity.ok().body(response);
	// }

	@GetMapping("/list")
	public ResponseEntity<MultiResponseDto<QuestionDto.Response>> getPassedQuestion(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Question> questionPage = questionService.getPassedQuestion(page);
		PageImpl<QuestionDto.Response> responses = questionMapper.questionsToPageResponse(questionPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), questionPage));
	}

	@DeleteMapping("/{question_id}/delete")
	public ResponseEntity<String> deleteQuestion(@PathVariable("question_id") Long questionId) {

		questionService.deleteQuestion(questionId);

		return ResponseEntity.noContent().build();
	}
}
