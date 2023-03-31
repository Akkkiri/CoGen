package ewha.backend.global.init;

import static ewha.backend.global.init.InitConstant.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.category.entity.Category;
import ewha.backend.domain.category.entity.CategoryType;
import ewha.backend.domain.category.repository.CategoryRepository;
import ewha.backend.domain.category.service.CategoryService;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.comment.repository.CommentRepository;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.feed.service.FeedService;
import ewha.backend.domain.qna.entity.Qna;
import ewha.backend.domain.qna.repository.QnaRepository;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.AnswerRepository;
import ewha.backend.domain.question.repository.QuestionRepository;
import ewha.backend.domain.question.service.QuestionService;
import ewha.backend.domain.quiz.entity.Quiz;
import ewha.backend.domain.quiz.repository.QuizRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;

@Configuration
public class Init {

	private static final Logger log = LoggerFactory.getLogger(Init.class);

	@Bean
	@Transactional
	CommandLineRunner stubInit(UserRepository userRepository, UserService userService,
		CategoryRepository categoryRepository, FeedRepository feedRepository,
		QuestionService questionService, QuestionRepository questionRepository,
		AnswerRepository answerRepository, QuizRepository quizRepository,
		FeedService feedService, CategoryService categoryService, QnaRepository qnaRepository,
		CommentRepository commentRepository, BCryptPasswordEncoder encoder) {

		for (int i = 0; i < 7; i++) {
			Category category = Category.builder()
				.categoryType(CategoryType.values()[i])
				.build();
			categoryRepository.save(category);
		}

		//         ------------------------------------------------------------------------------------------
		//         USER STUB
		//         ------------------------------------------------------------------------------------------

		User admin = User.builder()
			.userId("admin")
			.level(50)
			.ariFactor(0)
			.password(encoder.encode("admin"))
			.nickname("admin")
			.role(List.of("ROLE_ADMIN", "ROLE_USER"))
			.isFirstLogin(false)
			.build();

		userRepository.save(admin);

		List<User> userList = new ArrayList<>();

		for (int i = 1; i <= 20; i++) {

			Long rand = (long)(Math.random() * 50) + 1;
			Long rand2 = (long)(Math.random() * 50) + 1;

			User user = User.builder()
				.userId("asdfasdf" + i)
				.level(rand.intValue())
				.ariFactor(rand2.intValue())
				.password(encoder.encode("1234"))
				.nickname("닉네임" + i)
				.role(List.of("ROLE_USER"))
				.isFirstLogin(false)
				.genderType(GenderType.MALE)
				.ageType(AgeType.THIRTIES)
				.profileImage(InitConstant.DUMMY_IMAGE_LIST.get((int)(Math.random() * 6)))
				.thumbnailPath("thumbnail path")
				.build();

			userList.add(user);
		}

		log.info("USER STUB: " + userRepository.saveAll(userList));
		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         QNA STUB
		//         ------------------------------------------------------------------------------------------

		for (int i = 1; i <= 10; i++) {
			Qna qna = Qna.builder()
				.id((long)i)
				.content(InitConstant.QNA_BODY_LIST.get(i - 1))
				.build();
			qnaRepository.save(qna);
		}
		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         QUESTION STUB
		//         ------------------------------------------------------------------------------------------

		Collections.shuffle(InitConstant.DUMMY_QUESTION);
		LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		for (int i = 1; i <= 10; i++) {

			if (i == 1) {
				Question question = Question.builder()
					.content(InitConstant.DUMMY_QUESTION.get(0))
					.imagePath("https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/dummy/123.png")
					.openDate(thisMonday)
					.isOpened(true)
					.build();
				questionRepository.save(question);
				continue;
			}

			Question question = Question.builder()
				.content(InitConstant.DUMMY_QUESTION.get(i - 1))
				.imagePath("https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/dummy/123.png")
				.openDate(thisMonday.plusWeeks(i - 1))
				// .openDate(LocalDate.now().plusDays(i - 1))
				.isOpened(false)
				.build();
			questionRepository.save(question);

		}

		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         ANSWER STUB
		//         ------------------------------------------------------------------------------------------

		for (int i = 1; i <= 40; i++) {

			Long rand = (long)(Math.random() * 15);

			Answer answer = Answer.builder()
				.answerBody(DUMMY_ANSWER_LIST.get(rand.intValue()))
				.user(userService.findVerifiedUser((long)(Math.random() * 20) + 1))
				.question(questionService.findVerifiedQuestion((long)(Math.random() * 10) + 1))
				.likeCount((long)(Math.random() * 15))
				.reportCount((long)(Math.random() * 15))
				.build();

			answerRepository.save(answer);
		}

		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         QUIZ STUB
		//         ------------------------------------------------------------------------------------------

		Collections.shuffle(InitConstant.DUMMY_QUIZ_LIST);
		for (int i = 0; i < 20; i += 5) {
			if (i == 0) {
				for (int j = 0; j < 5; j++) {
					Quiz quiz = Quiz.builder()
						.content(DUMMY_QUIZ_LIST.get(j) + "의 뜻은 무엇일까요?")
						.answer("정답")
						.dummy1("더미 1")
						.dummy2("더미 2")
						.explanation("설명")
						.openDate(thisMonday)
						.isOpened(true)
						.build();
					quizRepository.save(quiz);
				}
				continue;
			}
			for (int j = i; j < i + 5; j++) {
				Quiz quiz = Quiz.builder()
					.content(DUMMY_QUIZ_LIST.get(j) + "의 뜻은 무엇일까요?")
					.answer("정답")
					.dummy1("더미 1")
					.dummy2("더미 2")
					.explanation("설명")
					.openDate(thisMonday.plusWeeks(j / 5))
					.isOpened(false)
					.build();
				quizRepository.save(quiz);
			}
		}

		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         FEED STUB
		//         ------------------------------------------------------------------------------------------

		List<Feed> feedList = new ArrayList<>();

		Collections.shuffle(InitConstant.FEED_BODY_LIST);
		Collections.shuffle(InitConstant.DUMMY_FEED_IMAGES);

		for (int i = 1; i <= 40; i++) {

			Long rand = (long)((Math.random() * 7) + 1);

			Category category = Category.builder()
				.id(rand)
				.categoryType(CategoryType.ETC)
				.build();

			Feed feed = Feed.builder()
				.category(category)
				.user(userService.findVerifiedUser((long)((Math.random() * 20) + 1)))
				.title("제목" + i)
				.imagePath(InitConstant.DUMMY_FEED_IMAGES.get(i - 1))
				.body(InitConstant.FEED_BODY_LIST.get(i - 1))
				.likeCount((long)(Math.random() * 50))
				.viewCount((long)(Math.random() * 100))
				.commentCount((long)(Math.random() * 10))
				.build();

			feedList.add(feed);
		}

		log.info("FEED STUB: " + feedRepository.saveAll(feedList));
		//         ------------------------------------------------------------------------------------------
		//         ------------------------------------------------------------------------------------------
		//         COMMENT STUB
		//         ------------------------------------------------------------------------------------------

		List<Comment> commentList = new ArrayList<>();

		for (int i = 1; i <= 80; i++) {

			Comment comment = Comment.builder()
				.body("comment body" + i)
				.user(userService.findVerifiedUser((long)(Math.random() * 20) + 1))
				.feed(feedService.findVerifiedFeed((long)(Math.random() * 40) + 1))
				.likeCount((long)(Math.random() * 10) + 1)
				.build();

			commentList.add(comment);
		}

		log.info("COMMENT STUB: " + commentRepository.saveAll(commentList));
		//         ------------------------------------------------------------------------------------------
		return null;
	}
}
