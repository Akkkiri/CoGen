package ewha.backend.Controller.constant;

import java.util.ArrayList;
import java.util.List;

import ewha.backend.domain.quiz.dto.QuizDto;

public class QuizControllerConstant {

	public static final QuizDto.Post POST_QUIZ_DTO =
		QuizDto.Post.builder()
			.content("퀴즈 내용")
			.explanation("퀴즈 설명")
			.answer("정답")
			.dummy1("더미 1")
			.dummy2("더미 2")
			.build();

	public static final QuizDto.Patch PATCH_QUIZ_DTO =
		QuizDto.Patch.builder()
			.content("퀴즈 내용")
			.explanation("퀴즈 설명")
			.answer("정답")
			.dummy1("더미 1")
			.dummy2("더미 2")
			.build();

	public static final QuizDto.Response QUIZ_RESPONSE_DTO =
		QuizDto.Response.builder()
			.content("퀴즈 내용")
			.explanation("퀴즈 설명")
			.bodies(List.of("정답", "오답", "오답"))
			.answer("정답")
			.build();

	public static final List<QuizDto.Response> QUIZ_RESPONSE_LIST =
		new ArrayList<>(List.of(QUIZ_RESPONSE_DTO, QUIZ_RESPONSE_DTO));
}
