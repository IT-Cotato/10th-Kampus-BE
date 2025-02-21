package com.cotato.kampus.domain.support.dto.response;

import com.cotato.kampus.domain.support.dto.UserInquiryDetail;

public record UserInquiryDetailResponse(
	UserInquiryDetail userInquiryDetail
) {
	public static UserInquiryDetailResponse from(UserInquiryDetail userInquiryDetail) {
		return new UserInquiryDetailResponse(
			userInquiryDetail
		);
	}
}
