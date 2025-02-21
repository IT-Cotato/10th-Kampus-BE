package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.verification.domain.VerificationPhoto;

public record VerificationPhotoDto(
	Long verificationRecordId,
	String imageUrl
) {
	public static VerificationPhotoDto from(VerificationPhoto verificationPhoto) {
		return new VerificationPhotoDto(
			verificationPhoto.getVerificationRecordId(), verificationPhoto.getImageUrl()
		);
	}
}
