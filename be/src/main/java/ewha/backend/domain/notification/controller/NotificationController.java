package ewha.backend.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.notification.dto.NotificationDto;
import ewha.backend.domain.notification.entity.Notification;
import ewha.backend.domain.notification.mapper.NotificationMapper;
import ewha.backend.domain.notification.service.NotificationService;
import ewha.backend.global.dto.ListResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;
	private final NotificationMapper notificationMapper;

	@GetMapping
	public ResponseEntity getMyNotifications() {

		List<Notification> notificationList = notificationService.getMyNotifications();
		List<NotificationDto.Response> responses = notificationMapper.myNotificationsToResponses(notificationList);

		return new ResponseEntity<>(
			new ListResponseDto<>(responses), HttpStatus.OK);
	}

	@GetMapping("/{notification_id}")
	public ResponseEntity<NotificationDto.Response> getMyNotification(@PathVariable("notification_id") Long notificationId) {

		Notification notification = notificationService.getMyNotification(notificationId);
		NotificationDto.Response response = notificationMapper.myNotificationToResponse(notification);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/check")
	public ResponseEntity<Boolean> checkIfNotReadNotifications() {

		Boolean response = notificationService.findIfNotReadNotifications();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{notification_id}/delete")
	public ResponseEntity<HttpStatus> deleteNotification(@PathVariable("notification_id") Long notificationId) {

		notificationService.deleteNotification(notificationId);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<HttpStatus> deleteAllMyNotifications() {

		notificationService.deleteAllMyNotifications();

		return ResponseEntity.noContent().build();
	}
}
