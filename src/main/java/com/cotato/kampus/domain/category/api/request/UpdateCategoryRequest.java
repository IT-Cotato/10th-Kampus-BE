package com.cotato.kampus.domain.category.api.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
	@NotBlank
	String categoryName
) {
}
