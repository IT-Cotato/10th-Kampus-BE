package com.cotato.kampus.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardCreateRequest(
	@NotBlank
	String boardName,
	String description,
	String universityName,
	@NotNull
	Boolean isCategoryRequired
) {
}
