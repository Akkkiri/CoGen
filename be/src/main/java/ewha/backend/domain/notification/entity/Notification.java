package ewha.backend.domain.notification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; // 수신자

	@Column
	private String url; // 알림 링크

	@Column
	private String body; // 알림 내용

	@Column
	private String receiverBody; // receiver 피드 제목 혹은 댓글 내용

	@Column(nullable = false)
	private Boolean isRead;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getNotifications().contains(this)) {
			this.user.getNotifications().add(this);
		}
	}

	public void setRead(Boolean read) {
		isRead = read;
	}
}
