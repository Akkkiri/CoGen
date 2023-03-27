package ewha.backend.domain.user.repository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import ewha.backend.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public void deleteNotVerifiedUsers() {
		jpaQueryFactory.delete(QUser.user)
			.where(QUser.user.isVerified.eq(false))
			.where(QUser.user.createdAt.month().eq(LocalDateTime.now().getMonthValue() - 3))
			.execute();
	}

	public User findByProviderAndProviderId(String provider, String providerId) {
		return jpaQueryFactory.selectFrom(QUser.user)
			.where(QUser.user.provider.eq(provider).and(QUser.user.providerId.eq(providerId)))
			.fetchOne();
	}
}
