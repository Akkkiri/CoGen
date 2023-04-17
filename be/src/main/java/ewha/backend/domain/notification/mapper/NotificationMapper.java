package ewha.backend.domain.notification.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import ewha.backend.domain.notification.dto.NotificationDto;
import ewha.backend.domain.notification.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

	default NotificationDto.Response myNotificationToResponse(Notification notification) {

		if (notification == null) {
			return null;
		}

		return NotificationDto.Response.builder()
			.notificationId(notification.getId())
			.type(notification.getType())
			.body(notification.getBody())
			.receiverBody(notification.getReceiverBody())
			// .receiverFeedTitle(notification.getReceiverFeedTitle())
			// .receiverCommentBody(notification.getReceiverCommentBody())
			.isRead(notification.getIsRead())
			.createdAt(notification.getCreatedAt())
			.build();

	}

	default List<NotificationDto.Response> myNotificationsToResponses(List<Notification> notificationList) {

		if (notificationList == null) {
			return null;
		}

		return notificationList.stream()
			.map(notification -> {
				return NotificationDto.Response.builder()
					.notificationId(notification.getId())
					.type(notification.getType())
					.body(notification.getBody())
					.receiverBody(notification.getReceiverBody())
					// .receiverFeedTitle(notification.getReceiverFeedTitle())
					// .receiverCommentBody(notification.getReceiverCommentBody())
					.isRead(notification.getIsRead())
					.createdAt(notification.getCreatedAt())
					.build();
			}).collect(Collectors.toList());
	}
}
