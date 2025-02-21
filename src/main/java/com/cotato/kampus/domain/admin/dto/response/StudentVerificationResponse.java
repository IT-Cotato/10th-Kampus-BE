package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.admin.dto.VerificationWithPhoto;
import com.cotato.kampus.domain.user.enums.VerificationStatus;

public record StudentVerificationResponse(
	Long verificationRecordId,
	Long universityId,
	String univName,
	VerificationStatus verificationStatus,
	String imageUrl
) {
	public static StudentVerificationResponse from(VerificationWithPhoto verification) {
		return new StudentVerificationResponse(
			verification.verificationRecordId(),
			verification.universityId(),
			verification.universityName(),
			verification.verificationStatus(),
			verification.imageUrl()
		);
	}

}
