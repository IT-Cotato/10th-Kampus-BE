package com.cotato.kampus.domain.verification.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.verification.dao.VerificationRecordRepository;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationRecordUpdater {

	private final VerificationRecordFinder verificationRecordFinder;
	private final VerificationRecordRepository verificationRecordRepository;

	@Transactional
	public void approve(Long verificationRecordId) {
		VerificationRecord verificationRecord = verificationRecordFinder.find(verificationRecordId);

		verificationRecord.setStatus(VerificationStatus.APPROVED);
	}

	@Transactional
	public void reject(Long verificationRecordId, String rejectionReason) {
		VerificationRecord verificationRecord = verificationRecordFinder.find(verificationRecordId);
		verificationRecord.setStatus(VerificationStatus.REJECTED);
		verificationRecord.setRejectionReason(rejectionReason);
	}
}
