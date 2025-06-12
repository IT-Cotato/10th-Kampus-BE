package com.cotato.kampus.domain.cert.api.response;

import com.cotato.kampus.domain.admin.dto.VerificationWithPhoto;
import com.cotato.kampus.domain.user.enums.VerificationStatus;

public record CertStatusResponse(
	Long verificationRecordId,
	String universityCode,
	VerificationStatus status,
	String rejectionReason
) {
	public static CertStatusResponse from(VerificationWithPhoto verification) {
		return new CertStatusResponse(
			verification.verificationRecordId(),
			verification.universityCode(),
			verification.verificationStatus(),
			verification.rejectReason()
		);
	}
}