package ewha.backend.Controller.constant;

import java.time.LocalDateTime;
import java.util.List;

import ewha.backend.domain.notification.dto.NotificationDto;
import ewha.backend.domain.notification.entity.NotificationType;

public class NotificationControllerConstant {

	public static final NotificationDto.Response NOTIFICATION_RESPONSE_DTO =
		NotificationDto.Response.builder()
			.notificationId(1L)
			.type(NotificationType.LIKE)
			.receiverBody("알림 내용")
			.isRead(true)
			.createdAt(LocalDateTime.now())
			.build();

	public static final List<NotificationDto.Response> NOTIFICATION_RESPONSE_LIST =
		List.of(NOTIFICATION_RESPONSE_DTO, NOTIFICATION_RESPONSE_DTO);
}
