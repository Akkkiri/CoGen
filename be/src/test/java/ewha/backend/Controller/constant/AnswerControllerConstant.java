package ewha.backend.Controller.constant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import ewha.backend.domain.question.dto.AnswerDto;

public class AnswerControllerConstant {

	public static final AnswerDto.Post POST_ANSWER_DTO =
		AnswerDto.Post.builder()
			.answerBody("답변 내용")
			.build();

	public static final AnswerDto.Patch PATCH_ANSWER_DTO =
		AnswerDto.Patch.builder()
			.answerBody("답변 내용")
			.build();

	public static final AnswerDto.Response ANSWER_RESPONSE_DTO =
		AnswerDto.Response.builder()
			.answerId(1L)
			.answerBody("답변 내용")
			.build();

	public static final AnswerDto.ListResponse ANSWER_LIST_RESPONSE =
		AnswerDto.ListResponse.builder()
			.answerId(1L)
			.nickname("사용자 닉네임")
			.profileImage("사용자 프로필 이미지")
			.thumbnailPath("썸네일 경로")
			.answerBody("답변 내용")
			.likeCount(10L)
			.reportCount(0L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<AnswerDto.ListResponse> ANSWER_LIST_PAGE_RESPONSE =
		new PageImpl<>(new ArrayList<>(List.of(ANSWER_LIST_RESPONSE, ANSWER_LIST_RESPONSE)));

	public static final AnswerDto.PageResponse ANSWER_PAGE_RESPONSE =
		AnswerDto.PageResponse.builder()
			.answerBody(List.of("답변 내용", "답변 내용"))
			.build();
}
