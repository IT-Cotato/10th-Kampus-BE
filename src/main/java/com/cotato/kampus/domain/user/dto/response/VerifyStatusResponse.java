package com.cotato.kampus.domain.user.dto.response;

import com.cotato.kampus.domain.user.enums.VerificationStatus;

public record VerifyStatusResponse(
	VerificationStatus status
) {
	public static VerifyStatusResponse from(VerificationStatus status){
		return new VerifyStatusResponse(
			status
		);
	}
}
