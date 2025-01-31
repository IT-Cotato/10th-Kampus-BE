package com.cotato.kampus.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardCreateRequest(
	@NotNull
	String boardName,
	Long universityId,
	@NotNull
	Boolean isCategoryRequired
) {
}
