package ewha.backend.domain.image.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedRepository;
import ewha.backend.domain.image.entity.Image;
import ewha.backend.domain.image.entity.ImageType;
import ewha.backend.domain.image.repository.ImageQueryRepository;
import ewha.backend.domain.image.repository.ImageRepository;
import ewha.backend.domain.question.entity.Question;
import ewha.backend.domain.question.repository.QuestionRepository;
import ewha.backend.domain.question.service.QuestionService;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsS3Service {
	private final AmazonS3Client amazonS3Client;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	// private final FeedService feedService;
	private final FeedRepository feedRepository;
	private final UserService userService;
	private final UserRepository userRepository;
	private final QuestionService questionService;
	private final QuestionRepository questionRepository;
	private final ImageQueryRepository imageQueryRepository;
	private final EntityManager em;
	private ImageType imageType;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public List<String> uploadImageToS3(MultipartFile multipartFile, Long id) throws Exception {

		HttpServletRequest httpServletRequest =
			((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		String requestURI = httpServletRequest.getRequestURI(); // 요청 URI. 피드 사진인지 프로필 사진인지 분기용

		String storedPath = null;

		Feed feed = Feed.builder().build();
		User user = User.builder().build();

		if (requestURI.contains("/feeds")) {
			storedPath = "feedImages/";
			imageType = ImageType.FEED;
			feed = feedRepository.findById(id)
				.orElseThrow(() ->
					new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
			// feed = feedService.findVerifiedFeed(id);
		} else if (requestURI.contains("/mypage")) {
			storedPath = "profileImages/";
			imageType = ImageType.PROFILE_PICTURE;
			user = userService.findVerifiedUser(id);
			user = userService.findVerifiedUser(id);
		}

		imageService.validateFileExists(multipartFile);

		if (multipartFile.isEmpty())
			return null;

		String originalImageName = multipartFile.getOriginalFilename(); // 원래 파일 이름

		String uuid = UUID.randomUUID().toString(); // 파일 이름으로 사용할 UUID 생성

		String extension = multipartFile.getContentType()
			.substring(multipartFile.getContentType().lastIndexOf("/") + 1); // 확장자 추출

		String[] extensions = {"png", "jpg", "jpeg"}; // 지원할 포맷

		if (!Arrays.asList(extensions).contains(extension.toLowerCase())) {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}

		String storedImageName = uuid + '.' + extension; // 파일 이름 + 확장자

		MultipartFile resizedFile = imageService.resizeImage(multipartFile, extension, storedImageName, requestURI);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(resizedFile.getSize());
		objectMetadata.setContentType(resizedFile.getContentType());

		InputStream inputStream = resizedFile.getInputStream();

		amazonS3Client.putObject(
			new PutObjectRequest(bucketName, storedPath + storedImageName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

		String fullPath = amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
		String thumbnailPath = uploadThumbnailToAwsS3(resizedFile, storedImageName);

		Image.ImageBuilder image = Image.builder();

		image.originalImageName(originalImageName)
			.storedImageName(storedImageName)
			.storedPath(fullPath);

		if (requestURI.contains("/feeds")) {

			Feed findFeed = feed = feedRepository.findById(id)
				.orElseThrow(() ->
					new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
			// Feed findFeed = feedService.findVerifiedFeed(id);
			image.imageType(ImageType.FEED)
				.thumbnailPath(thumbnailPath)
				.feed(findFeed)
				.user(findFeed.getUser());

			findFeed.addImagePaths(fullPath, thumbnailPath);

			feedRepository.save(findFeed);

		} else if (requestURI.contains("/mypage")) {

			User findUser = userService.findVerifiedUser(id);

			image.imageType(ImageType.PROFILE_PICTURE)
				.thumbnailPath(thumbnailPath)
				.user(findUser)
				.feed(null);

			findUser.setProfileImage(fullPath);
			findUser.setThumbnailPath(thumbnailPath);

			userRepository.save(findUser);
		}

		Image storedImage = imageRepository.save(image.build());

		return List.of(fullPath, thumbnailPath);
		//        return amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
	}

	public List<String> uploadVideoToS3(MultipartFile multipartFile, Long id) throws Exception {

		HttpServletRequest httpServletRequest =
			((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		String requestURI = httpServletRequest.getRequestURI(); // 요청 URI. 피드 사진인지 프로필 사진인지 분기용

		String storedPath = null;

		if (requestURI.contains("/mypage")) {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}

		storedPath = "feedVideos/";
		imageType = ImageType.FEED;
		Feed feed = feed = feedRepository.findById(id)
			.orElseThrow(() ->
				new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
		// Feed feed = feedService.findVerifiedFeed(id);

		String originalImageName = multipartFile.getOriginalFilename(); // 원래 파일 이름

		String uuid = UUID.randomUUID().toString(); // 파일 이름으로 사용할 UUID 생성

		String extension = multipartFile.getContentType()
			.substring(multipartFile.getContentType().lastIndexOf("/") + 1); // 확장자 추출

		String[] extensions = {"mp4", "jpg", "jpeg"}; // 지원할 포맷

		if (!Arrays.asList(extensions).contains(extension.toLowerCase())) {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}

		String storedImageName = uuid + '.' + extension; // 파일 이름 + 확장자

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		InputStream inputStream = multipartFile.getInputStream();

		amazonS3Client.putObject(
			new PutObjectRequest(bucketName, storedPath + storedImageName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

		String fullPath = amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();

		Image.ImageBuilder image = Image.builder();

		image.originalImageName(originalImageName)
			.storedImageName(storedImageName)
			.storedPath(fullPath)
			.imageType(ImageType.FEED)
			.feed(feed)
			.user(feed.getUser());

		String thumbnailPath = "";

		feed.addImagePaths(fullPath, thumbnailPath);

		feedRepository.save(feed);

		Image storedImage = imageRepository.save(image.build());

		return List.of(fullPath, thumbnailPath);

	}

	public List<String> uploadQuestionImageToS3(MultipartFile multipartFile, Long id) throws Exception {

		HttpServletRequest httpServletRequest =
			((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		String requestURI = httpServletRequest.getRequestURI(); // 요청 URI. 피드 사진인지 프로필 사진인지 분기용

		String storedPath = "questionImages/";

		Question question = questionService.findVerifiedQuestion(id);

		imageService.validateFileExists(multipartFile);

		if (multipartFile.isEmpty())
			throw new BusinessLogicException(ExceptionCode.IMAGE_IS_EMPTY);

		String originalImageName = multipartFile.getOriginalFilename(); // 원래 파일 이름

		String uuid = UUID.randomUUID().toString(); // 파일 이름으로 사용할 UUID 생성

		String extension = multipartFile.getContentType()
			.substring(multipartFile.getContentType().lastIndexOf("/") + 1); // 확장자 추출

		String[] extensions = {"png", "jpg", "jpeg"}; // 지원할 포맷

		if (!Arrays.asList(extensions).contains(extension)) {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}

		String storedImageName = uuid + '.' + extension; // 파일 이름 + 확장자

		MultipartFile resizedFile = imageService.resizeImage(multipartFile, extension, storedImageName, requestURI);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(resizedFile.getSize());
		objectMetadata.setContentType(resizedFile.getContentType());

		InputStream inputStream = resizedFile.getInputStream();

		amazonS3Client.putObject(
			new PutObjectRequest(bucketName, storedPath + storedImageName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

		String fullPath = amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
		String thumbnailPath = uploadThumbnailToAwsS3(resizedFile, storedImageName);

		Image.ImageBuilder image = Image.builder();

		image.originalImageName(originalImageName)
			.storedImageName(storedImageName)
			.storedPath(fullPath);

		image.imageType(ImageType.QUESTION)
			.thumbnailPath(thumbnailPath)
			.question(question);

		question.addImagePaths(fullPath, thumbnailPath);

		questionRepository.save(question);

		Image storedImage = imageRepository.save(image.build());

		return List.of(fullPath, thumbnailPath);

		//        return amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
	}

	public List<String> updateORDeleteFeedImageFromS3(Long feedId, MultipartFile multipartFile) throws Exception {

		Feed findFeed = feedRepository.findById(feedId)
			.orElseThrow(() ->
				new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
		// Feed findFeed = feedService.findVerifiedFeed(feedId);
		String feedImagePath = findFeed.getImagePath();
		String thumbnailImagePath = findFeed.getThumbnailPath();
		String imageName = feedImagePath.substring(feedImagePath.lastIndexOf("/"));
		String thumbnailName = thumbnailImagePath.substring(thumbnailImagePath.lastIndexOf("/"));
		List<String> newImagePath = null;

		if (feedImagePath == null && multipartFile != null) {
			newImagePath = uploadImageToS3(multipartFile, feedId);
		} else if (feedImagePath != null && multipartFile != null) {
			deleteImageFromS3(imageName);
			newImagePath = uploadImageToS3(multipartFile, feedId);
		} else if (multipartFile == null) {
			deleteImageFromS3(imageName);
			// deleteImageFromS3(thumbnailName);
		}

		return newImagePath;
	}

	public List<String> updateORDeleteUserImageFromS3(Long userId, MultipartFile multipartFile) throws Exception {

		User findUser = userService.findVerifiedUser(userId);

		String userImagePath = findUser.getProfileImage();
		String thumbnailPath = findUser.getThumbnailPath();;
		String imageName = userImagePath.substring(userImagePath.lastIndexOf("/"));
		String thumbnailName = thumbnailPath.substring(thumbnailPath.lastIndexOf("/"));
		List<String> newImagePath = null;

		if (userImagePath == null && multipartFile != null) {
			newImagePath = uploadImageToS3(multipartFile, userId);
		} else if (userImagePath != null && multipartFile != null) {
			deleteImageFromS3(imageName);
			newImagePath = uploadImageToS3(multipartFile, userId);
		} else if (multipartFile == null) {
			deleteImageFromS3(imageName);
			// deleteImageFromS3(thumbnailName);
		}

		return newImagePath;
	}

	public List<String> updateORDeleteQuestionImageFromS3(Long questionId, MultipartFile multipartFile) throws
		Exception {

		Question findQuestion = questionService.findVerifiedQuestion(questionId);
		String questionImagePath = findQuestion.getImagePath();
		String questionThumbnailPath = findQuestion.getThumbnailPath();
		String imageName = questionImagePath.substring(questionImagePath.lastIndexOf("/"));
		String thumbnailName = questionThumbnailPath.substring(questionThumbnailPath.lastIndexOf("/"));
		List<String> newImagePath = null;

		if (questionImagePath == null && multipartFile != null) {
			newImagePath = uploadQuestionImageToS3(multipartFile, questionId);
		} else if (questionImagePath != null && multipartFile != null) {
			deleteImageFromS3(imageName);
			newImagePath = uploadQuestionImageToS3(multipartFile, questionId);
		} else if (multipartFile == null) {
			deleteImageFromS3(imageName);
			// deleteImageFromS3(thumbnailName);
		}

		return newImagePath;
	}

	public String uploadThumbnailToAwsS3(MultipartFile multipartFile, String storedImageName) throws IOException {

		BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
		Integer getWidth = bufferedImage.getWidth();
		Integer getHeight = bufferedImage.getHeight();
		Double ratio = (double)(getWidth / getHeight);

		BufferedImage thumbnailImage = Thumbnails.of(bufferedImage)
			.size(100, 100)
			.asBufferedImage();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(thumbnailImage, "png", os);

		byte[] bytes = os.toByteArray();

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(bytes.length);
		objectMetadata.setContentType("image/png");

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
			"thumbnailImages/" + "s_" + storedImageName, byteArrayInputStream, objectMetadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3Client.putObject(putObjectRequest);

		return amazonS3Client.getUrl(bucketName, "thumbnailImages/" + "s_" + storedImageName).toString();
	}

	public void deleteImageFromS3(String imageName) {

		Image findImage = imageService.findImageByStoredImageName(imageName.substring(1));

		// imageRepository.deleteById(findImage.getId());
		imageQueryRepository.deleteById(findImage.getId());


		amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, findImage.getStoredPath()));
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, findImage.getThumbnailPath()));

		em.flush();
	}
}
