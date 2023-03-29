package ewha.backend.Controller.constant;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.question.dto.QuestionDto;

public class QuestionControllerConstant {

	public static final QuestionDto.Post POST_QUESTION_DTO =
		QuestionDto.Post.builder()
			.content("이번주 질문 내용")
			.build();

	public static final QuestionDto.Patch PATCH_QUESTION_DTO =
		QuestionDto.Patch.builder()
			.content("이번주 질문 내용")
			.imagePath("이미지 주소")
			.thumbnailPath("썸네일 주소")
			.build();

	public static final QuestionDto.Response QUESTION_RESPONSE_DTO =
		QuestionDto.Response.builder()
			.questionId(1L)
			.content("이번주 질문 내용")
			.imagePath("이미지 주소")
			.thumbnailPath("썸네일 주소")
			.build();

	public static final PageImpl<QuestionDto.Response> QUESTION_RESPONSE_PAGE =
		new PageImpl<>(List.of(QUESTION_RESPONSE_DTO, QUESTION_RESPONSE_DTO));
}
