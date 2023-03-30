package ewha.backend.global.security.event;

import org.springframework.context.ApplicationEvent;

import ewha.backend.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserRegistrationApplicationEvent extends ApplicationEvent {
	private User user;

	public UserRegistrationApplicationEvent(Object source, User user) {
		super(source);
		this.user = user;
	}
}
