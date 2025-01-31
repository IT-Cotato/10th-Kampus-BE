package com.cotato.kampus.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardCreateRequest(
	@NotNull
	String boardName,
	String description,
	Long universityId,
	@NotNull
	Boolean isCategoryRequired
) {
}
