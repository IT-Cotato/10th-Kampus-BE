package com.cotato.kampus.domain.admin.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.VerificationPhotoDto;
import com.cotato.kampus.domain.verification.dao.VerificationPhotoRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationPhotoFinder {

	private final VerificationPhotoRepository verificationPhotoRepository;

	public VerificationPhotoDto findByRecordId(Long verificationRecordId) {
		return VerificationPhotoDto.from(
			verificationPhotoRepository.findByVerificationRecordId(verificationRecordId)
				.orElseThrow(() -> new AppException(ErrorCode.VERIFICATION_PHOTO_NOT_FOUND))
		);
	}
}
