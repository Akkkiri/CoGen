package ewha.backend.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ewha.backend.domain.notification.controller.SseController;
import ewha.backend.domain.notification.entity.Notification;
import ewha.backend.domain.notification.entity.NotificationType;
import ewha.backend.domain.notification.repository.EmitterRepository;
import ewha.backend.domain.notification.repository.NotificationQueryRepository;
import ewha.backend.domain.notification.repository.NotificationRepository;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final EmitterRepository emitterRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationQueryRepository notificationQueryRepository;
	private final UserService userService;
	private static final Long DEFAULT_TIMEOUT = 180000L;

	@Override
	public SseEmitter connect(String lastEventId) {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();
		String connectId = userId + "_" + System.currentTimeMillis();
		SseEmitter sseEmitter = emitterRepository.save(connectId, new SseEmitter(DEFAULT_TIMEOUT));

		// 완료, 시간초과, 에러로 인해 요청 전송 불가시 저장해둔 sseEmitter 삭제
		sseEmitter.onCompletion(() -> emitterRepository.deleteById(connectId));
		sseEmitter.onTimeout(() -> emitterRepository.deleteById(connectId));
		sseEmitter.onError((e) -> emitterRepository.deleteById(connectId));

		sendToClient(sseEmitter, connectId, "Connected. [userId=" + userId + "]"); // 재연결 요청시 503 방지를 위한 더미 데이터

		if (!lastEventId.isEmpty()) {
			final SseEmitter finalSseEmitter = sseEmitter; // sseEmitter를 final 변수로 선언
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
			events.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendToClient(finalSseEmitter, entry.getKey(), entry.getValue()));
		}

		// if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방
		// 	Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
		// 	events.entrySet().stream()
		// 		.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
		// 		.forEach(entry -> sendToClient(sseEmitter, entry.getKey(), "test"));
		// 		// .forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getValue()));
		// }

		return sseEmitter;
	}

	private void sendToClient(SseEmitter emitter, String id, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(id)
				.name("sse")
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(id);
			throw new RuntimeException("연결 오류");
		}
	}

	@Override
	@Transactional
	public void send(User user, String url, String body, String content, NotificationType notificationType) {

		Notification notification = createNotification(user, url, body, content, notificationType);
		String userId = String.valueOf(user.getId());
		notificationRepository.save(notification);
		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(userId);

		sseEmitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, notification);
				sendToClient(emitter, key, notification);
			}
		);
	}

	private Notification createNotification(User user, String url,
		String body, String content, NotificationType notificationType) {

		Notification notification = Notification.builder()
			.user(user)
			.type(notificationType)
			.url(url)
			.isRead(false)
			.body(body)
			.receiverBody(content)
			.build();

		return notificationRepository.save(notification);
	}

	@Override
	@Transactional
	public void notifyMessagingEvent(User user) { // 메세지 알림

		User findUser = userService.getLoginUser();

		Long userId = findUser.getId();

		if (SseController.sseEmitters.containsKey(userId)) {
			SseEmitter sseEmitter = SseController.sseEmitters.get(userId);
			try {
				sseEmitter.send(SseEmitter.event().name("save")
					.data("메시지가 도착했습니다."));
			} catch (Exception e) {
				SseController.sseEmitters.remove(userId);
			}
		}

		Notification notification = Notification.builder()
			.user(findUser)
			.type(NotificationType.MESSAGE)
			.body("메시지가 도착했습니다.")
			.isRead(false)
			.build();

		notificationRepository.save(notification);
	}

	@Override
	@Transactional
	public Notification getMyNotification(Long notificationId) {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();
		Notification findNotification = notificationQueryRepository.getMyNotification(userId, notificationId);
		findNotification.setRead(true);

		return notificationRepository.save(findNotification);
	}

	@Override
	@Transactional
	public List<Notification> getMyNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		List<Notification> response =  notificationQueryRepository.getMyNotifications(userId);

		response.forEach(notification -> notification.setRead(true));

		return response;
	}

	@Override
	@Transactional
	public Boolean findIfNotReadNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		return notificationQueryRepository.findIfNotReadNotifications(userId);
	}

	@Override
	@Transactional
	public void deleteNotification(Long notificationId) {

		User findUser = userService.getLoginUser();

		verifyNotification(findUser, notificationId);

		notificationQueryRepository.deleteNotifications(notificationId);

	}

	@Override
	@Transactional
	public void deleteAllMyNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		notificationQueryRepository.deleteAllMyNotifications(userId);
	}

	public void verifyNotification(User user, Long notificationId) {
		if (notificationQueryRepository.findNotificationByUserAndNotificationId(user, notificationId) == null) {
			throw new BusinessLogicException(ExceptionCode.NOTIFICATION_NOT_FOUND);
		}
	}
}
