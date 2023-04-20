package ewha.backend.domain.image.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsS3ServiceForDeletion {

	private final AmazonS3Client amazonS3Client;
	private final EntityManager em;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public void deleteProfileImageFromS3(String profileImage) {

		String extracted = profileImage.substring(profileImage.indexOf("profileImages/"));

		amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, extracted));

		em.flush();
	}
}
