package com.cotato.kampus.domain.product.api.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductCategoryRequest(
	@NotBlank
	String categoryName
) {
}
