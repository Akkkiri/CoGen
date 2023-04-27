package ewha.backend.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	void deleteById(Long imageId);
}
