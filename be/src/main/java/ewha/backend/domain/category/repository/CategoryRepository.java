package ewha.backend.domain.category.repository;

import ewha.backend.domain.category.entity.Category;
import ewha.backend.domain.category.entity.CategoryType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	//    @Query(nativeQuery = true, value = "SELECT * " +
	//            "FROM CATEGORY " + "WHERE CATEGORY_TYPE = :categoryType")
	Optional<Category> findCategoryByCategoryType(CategoryType categoryType);
}
