package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;

public record VerificationWithPhoto(
	Long verificationRecordId,
	Long universityId,
	String universityCode,
	VerificationType verificationType,
	VerificationStatus verificationStatus,
	String imageUrl,
	String rejectReason
) {
	public static VerificationWithPhoto of(VerificationRecordDto verificationRecordDto, String universityCode,
		VerificationPhotoDto verificationPhotoDto) {
		return new VerificationWithPhoto(
			verificationRecordDto.verificationRecordId(),
			verificationRecordDto.universityId(),
			universityCode,
			verificationRecordDto.verificationType(),
			verificationRecordDto.verificationStatus(),
			verificationPhotoDto != null ? verificationPhotoDto.imageUrl() : null,
			verificationRecordDto.rejectReason()
		);
	}
}
