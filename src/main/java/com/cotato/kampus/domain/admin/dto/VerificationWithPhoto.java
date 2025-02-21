package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;

public record VerificationWithPhoto(
	Long verificationRecordId,
	Long universityId,
	String universityName,
	VerificationStatus verificationStatus,
	String imageUrl
) {
	public static VerificationWithPhoto of(VerificationRecordDto verificationRecordDto, String universityName,
		VerificationPhotoDto verificationPhotoDto) {
		return new VerificationWithPhoto(
			verificationRecordDto.id(),
			verificationRecordDto.universityId(),
			universityName,
			verificationRecordDto.verificationStatus(),
			verificationPhotoDto.imageUrl()
		);
	}
}
