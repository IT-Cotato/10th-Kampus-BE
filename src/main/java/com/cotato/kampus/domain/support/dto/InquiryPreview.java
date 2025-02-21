package com.cotato.kampus.domain.support.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.support.enums.InquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record InquiryPreview(
	Long inquiryId,
	String title,
	InquiryStatus status,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime
) {
	public static InquiryPreview from(InquiryDto inquiryDto) {
		return new InquiryPreview(
			inquiryDto.inquiryId(),
			inquiryDto.title(),
			inquiryDto.inquiryStatus(),
			inquiryDto.createdTime()
		);
	}
}
