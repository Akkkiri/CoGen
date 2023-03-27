package ewha.backend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ewha.backend.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(String userId);

	User findByNickname(String nickname);

	Optional<User> findByEmail(String email);

	User findByProviderId(String providerId);

	Boolean existsByUserId(String userId);

	Boolean existsByNickname(String nickname);
}
