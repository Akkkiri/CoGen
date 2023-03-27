package ewha.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.user.entity.UserQna;

public interface UserQnaRepository extends JpaRepository<UserQna, Long> {
}
