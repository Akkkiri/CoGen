package ewha.backend.domain.notification.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.notification.entity.Notification;
import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.notification.entity.QNotification;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public Notification getMyNotification(Long userId, Long notificationId) {

		return jpaQueryFactory
			.selectFrom(QNotification.notification)
			.where(QNotification.notification.user.id.eq(userId).and(QNotification.notification.id.eq(notificationId)))
			.fetchOne();
	}

	public List<Notification> getMyNotifications(Long userId) {

		return jpaQueryFactory
			.selectFrom(QNotification.notification)
			.where(QNotification.notification.user.id.eq(userId))
			.fetch();
	}

	public Boolean findIfNotReadNotifications(Long userId) {

		Long size = jpaQueryFactory
			.select(QNotification.notification.count())
			.from(QNotification.notification)
			.where(QNotification.notification.user.id.eq(userId).and(QNotification.notification.isRead.eq(false)))
			.fetchOne();

		if (size == 0) {
			return false;
		} else {
			return true;
		}

	}

	public void deleteNotifications(Long notificationId) {

		jpaQueryFactory
			.delete(QNotification.notification)
			.where(QNotification.notification.id.eq(notificationId))
			.execute();
	}

	public void deleteAllMyNotifications(Long userId) {

		jpaQueryFactory
			.delete(QNotification.notification)
			.where(QNotification.notification.user.id.eq(userId))
			.execute();
	}

	public Notification findNotificationByUserAndNotificationId(User findUser, Long notificationId) {

		return jpaQueryFactory
			.selectFrom(QNotification.notification)
			.where(QNotification.notification.user.eq(findUser).and(QNotification.notification.id.eq(notificationId)))
			.fetchOne();
	}
}
