package com.cotato.kampus.domain.product.api.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductCategoryRequest(

	@NotBlank(message = "카테고리 이름은 필수 항목입니다")
	String categoryName
) {
}
