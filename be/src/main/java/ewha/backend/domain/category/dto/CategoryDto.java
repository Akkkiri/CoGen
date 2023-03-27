package ewha.backend.domain.category.dto;

import javax.validation.constraints.NotBlank;

import ewha.backend.domain.category.entity.CategoryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		@NotBlank
		private CategoryType categoryType;
	}
}
