package com.cotato.kampus.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record BoardUpdateRequest(
	@NotNull
	String boardName,
	String description,
	@NotNull
	Boolean isCategoryRequired
) {
}
