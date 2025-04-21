package com.cotato.kampus.domain.admin.dto.request;

import java.util.List;

import com.cotato.kampus.domain.board.enums.BoardType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardCreateRequest(
	@NotBlank
	String boardName,
	@NotBlank
	String description,
	@NotNull
	BoardType boardType,
	String universityCode,
	List<String> categories
) {
}
