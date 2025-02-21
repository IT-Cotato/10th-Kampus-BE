package com.cotato.kampus.domain.support.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.support.domain.Inquiry;
import com.cotato.kampus.domain.support.enums.InquiryStatus;

public record InquiryDto(
	Long inquiryId,
	Long userId,
	String title,
	String content,
	InquiryStatus inquiryStatus,
	LocalDateTime createdTime
) {
	public static InquiryDto from(Inquiry inquiry) {
		return new InquiryDto(
			inquiry.getId(),
			inquiry.getUserId(),
			inquiry.getTitle(),
			inquiry.getContent(),
			inquiry.getInquiryStatus(),
			inquiry.getCreatedTime()
		);
	}
}
