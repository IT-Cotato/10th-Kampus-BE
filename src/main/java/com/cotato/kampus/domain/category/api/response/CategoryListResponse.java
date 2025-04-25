package com.cotato.kampus.domain.category.api.response;

import java.util.List;

import com.cotato.kampus.domain.category.domain.Category;

public record CategoryListResponse(
	List<Category> categories
) {
	public static CategoryListResponse from(List<Category> categories) {
		return new CategoryListResponse(categories);
	}
}
