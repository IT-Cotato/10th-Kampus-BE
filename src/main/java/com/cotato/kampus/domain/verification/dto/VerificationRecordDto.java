package com.cotato.kampus.domain.verification.dto;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;

public record VerificationRecordDto(
	Long verificationRecordId,
	Long userId,
	Long universityId,
	VerificationType verificationType,
	VerificationStatus verificationStatus,
	String rejectReason
) {
	public static VerificationRecordDto from(VerificationRecord verificationRecord) {
		return new VerificationRecordDto(
			verificationRecord.getId(),
			verificationRecord.getUserId(),
			verificationRecord.getUniversityId(),
			verificationRecord.getVerificationType(),
			verificationRecord.getVerificationStatus(),
			verificationRecord.getRejectionReason()
		);
	}
}
