package ewha.backend.domain.image.repository;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.image.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.image.entity.QImage;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImageQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Image findByFeedId(Long feedId) {

		return jpaQueryFactory
			.selectFrom(QImage.image)
			.where(QImage.image.feed.id.eq(feedId))
			.fetchFirst();
	}

	public Image findByUserId(Long userId) {

		return jpaQueryFactory
			.selectFrom(QImage.image)
			.where(QImage.image.user.id.eq(userId))
			.fetchFirst();
	}

	public void deleteById(Long imageId) {

		jpaQueryFactory
			.delete(QImage.image)
			.where(QImage.image.id.eq(imageId))
			.execute();
	}
	public void deleteByFeedId(Long feedId) {

		jpaQueryFactory
			.delete(QImage.image)
			.where(QImage.image.feed.id.eq(feedId))
			.execute();
	}

	public void deleteByImagePath(String imagePath) {

		jpaQueryFactory
			.delete(QImage.image)
			.where(QImage.image.storedPath.eq(imagePath))
			.execute();
	}

	public Image findByStoredImageName(String imageName) {

		return jpaQueryFactory
			.selectFrom(QImage.image)
			.where(QImage.image.storedImageName.eq(imageName))
			.fetchOne();
	}
}
