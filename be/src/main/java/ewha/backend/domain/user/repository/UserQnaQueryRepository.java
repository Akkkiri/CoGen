package ewha.backend.domain.user.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQnaQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;
}
