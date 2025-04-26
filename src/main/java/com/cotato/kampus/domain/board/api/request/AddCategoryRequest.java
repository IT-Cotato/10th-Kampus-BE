package com.cotato.kampus.domain.board.api.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AddCategoryRequest(
	@NotEmpty
	List<@NotEmpty String> categoryNames
) {
}
