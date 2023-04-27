package ewha.backend.domain.category.service;

import ewha.backend.domain.category.entity.Category;
import ewha.backend.domain.category.entity.CategoryType;
import ewha.backend.domain.category.repository.CategoryRepository;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public Category findVerifiedCategory(CategoryType categoryType) {
		return categoryRepository.findCategoryByCategoryType(categoryType)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
	}
}
