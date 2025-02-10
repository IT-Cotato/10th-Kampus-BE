package com.cotato.kampus.domain.verification.dto;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;

public record VerificationRecordDto(
	Long id,
	Long userId,
	Long universityId,
	VerificationType verificationType,
	VerificationStatus verificationStatus
) {
	public static VerificationRecordDto from(VerificationRecord verificationRecord) {
		return new VerificationRecordDto(
			verificationRecord.getId(),
			verificationRecord.getUserId(),
			verificationRecord.getUniversityId(),
			verificationRecord.getVerificationType(),
			verificationRecord.getVerificationStatus()
		);
	}
}
