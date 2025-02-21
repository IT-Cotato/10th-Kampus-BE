package com.cotato.kampus.domain.support.dto;

import java.util.List;

import com.cotato.kampus.domain.support.domain.InquiryReply;
import com.cotato.kampus.domain.support.enums.InquiryStatus;

public record UserInquiryDetail(
	Long inquiryId,
	String title,
	String content,
	List<String> photoUrls,
	InquiryStatus status,
	String adminReplyContent
) {
	public static UserInquiryDetail from(InquiryDto inquiryDto, List<String> photoUrls, InquiryReply inquiryReply) {
		return new UserInquiryDetail(
			inquiryDto.inquiryId(),
			inquiryDto.title(),
			inquiryDto.content(),
			photoUrls,
			inquiryDto.inquiryStatus(),
			inquiryReply != null ? inquiryReply.getContent() : null
		);
	}
}
