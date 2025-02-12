package com.cotato.kampus.domain.verification.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.verification.dao.VerificationRecordRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationRecordAppender {

	private final ApiUserResolver apiUserResolver;
	private final VerificationRecordRepository verificationRecordRepository;


	@Transactional
	public Long appendEmailType(Long universityId) {
		User user = apiUserResolver.getUser();
		VerificationRecord verificationRecord = VerificationRecord.builder()
			.userId(user.getId())
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
}
