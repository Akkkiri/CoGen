package ewha.backend.global.aop;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ewha.backend.domain.user.repository.UserQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

	private final UserQueryRepository userQueryRepository;

	@Scheduled(cron = " 0 10 0 1 1/1 ? *")
	public void deleteNotVerifiedUsersWithinThreeMonths() {
		userQueryRepository.deleteNotVerifiedUsers();
	}
}
