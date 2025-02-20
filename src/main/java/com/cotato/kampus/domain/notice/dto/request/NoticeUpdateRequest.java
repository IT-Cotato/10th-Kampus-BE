package com.cotato.kampus.domain.notice.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record NoticeUpdateRequest(
	@NotBlank @Length(max = 50)
	String title,
	@NotBlank @Length(max = 1000)
	String content
) {
}
