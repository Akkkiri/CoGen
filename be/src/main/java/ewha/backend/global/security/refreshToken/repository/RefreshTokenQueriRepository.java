package ewha.backend.global.security.refreshToken.repository;

import static ewha.backend.global.security.refreshToken.entity.QRefreshToken.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenQueriRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public String findUserIdByTokenValue(String value) {
		return jpaQueryFactory.select(refreshToken.userId)
			.from(refreshToken)
			.where(refreshToken.tokenValue.eq(value))
			.fetchOne();
	}
}
