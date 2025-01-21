package com.cotato.kampus.global.common.dto;

import org.springframework.data.domain.Sort;

public record CustomPageRequest(
	int page,
	int size,
	Sort.Direction direction
) {
	public CustomPageRequest {
		// Validation
		if (page <= 0) {
			page = 1;
		}
		final int DEFAULT_SIZE = 10;
		final int MAX_SIZE = 50;
		size = Math.min(size, MAX_SIZE) > 0 ? size : DEFAULT_SIZE;
	}

	public org.springframework.data.domain.PageRequest of(String sortProperty) {
		return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, sortProperty);
	}
}
