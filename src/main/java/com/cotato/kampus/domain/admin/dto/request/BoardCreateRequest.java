package com.cotato.kampus.domain.admin.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record BoardCreateRequest(
	@NotBlank
	String boardName,
	String description,
	String universityCode,
	List<String> categories
) {
}
