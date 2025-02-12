package com.cotato.kampus.domain.admin.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.verification.domain.VerificationRecord;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;

public record StudentVerification(
	Long verificationRecordId,
	LocalDateTime joinDate,
	String univName,
	VerificationType verificationType,
	VerificationStatus verificationStatus
) {
	public static StudentVerification of(VerificationRecord verificationRecord, String univName) {
		return new StudentVerification(
			verificationRecord.getId(),
			verificationRecord.getCreatedTime(),
			univName,
			verificationRecord.getVerificationType(),
			verificationRecord.getVerificationStatus()
		);
	}
}
