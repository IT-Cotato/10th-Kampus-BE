package com.cotato.kampus.domain.cert.api.response;

import com.cotato.kampus.domain.admin.dto.VerificationWithPhoto;
import com.cotato.kampus.domain.user.enums.VerificationStatus;

public record RejectReasonResponse(
	Long verificationRecordId,
	String imageUrl,
	String rejectionReason,
	VerificationStatus status
) {
	public static RejectReasonResponse of(VerificationWithPhoto verification) {
		return new RejectReasonResponse(
			verification.verificationRecordId(),
			verification.imageUrl(),
			verification.rejectReason(),
			verification.verificationStatus()
		);
	}
}
