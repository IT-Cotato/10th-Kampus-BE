package com.cotato.kampus.domain.support.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.support.dto.InquiryPreview;

public record UserInquiryListResponse(
	List<InquiryPreview> inquiryPreviews,
	Boolean hasNext
) {
	public static UserInquiryListResponse from(Slice<InquiryPreview> inquirySlice) {
		return new UserInquiryListResponse(
			inquirySlice.getContent(),
			inquirySlice.hasNext()
		);
	}
}