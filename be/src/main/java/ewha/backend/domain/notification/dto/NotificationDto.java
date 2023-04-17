package ewha.backend.domain.notification.dto;

import java.time.LocalDateTime;

import ewha.backend.domain.notification.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NotificationDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long notificationId;
		private NotificationType type;
		private String body;
		// private String receiverFeedTitle;
		// private String receiverCommentBody;
		private String receiverBody;
		private Boolean isRead;
		private LocalDateTime createdAt;
	}
}
