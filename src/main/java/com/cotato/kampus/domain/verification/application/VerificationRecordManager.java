package com.cotato.kampus.domain.verification.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.verification.dao.VerificationRecordRepository;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationRecordManager {

	private final VerificationRecordRepository verificationRecordRepository;


	@Transactional
	public Long appendEmailType(Long userId, Long universityId) {
		VerificationRecord verificationRecord = VerificationRecord.builder()
			.userId(userId)
			.universityId(universityId)
			.verificationStatus(VerificationStatus.APPROVED)
			.verificationType(VerificationType.EMAIL).build();

		return verificationRecordRepository.save(verificationRecord).getId();
	}

	@Transactional
	public Long appendPhotoType(Long userId, Long universityId) {
		VerificationRecord verificationRecord = VerificationRecord.builder()
			.userId(userId)
			.universityId(universityId)
			.verificationStatus(VerificationStatus.PENDING)
			.verificationType(VerificationType.PHOTO).build();

		return verificationRecordRepository.save(verificationRecord).getId();
	}

	@Transactional
	public void deleteAllByUserId(Long userId) {
		verificationRecordRepository.deleteAllByUserId(userId);
	}
}
