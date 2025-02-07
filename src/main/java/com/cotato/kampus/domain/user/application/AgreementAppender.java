package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.dao.AgreementRepository;
import com.cotato.kampus.domain.user.domain.Agreement;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementAppender {

	private final AgreementRepository agreementRepository;

	@Transactional
	public Long appendAgreement(Long userId, boolean personalInfoAgreement, boolean privacyPolicyAgreement,
		boolean termsOfServiceAgreement, boolean marketingAgreement) {
		if (agreementRepository.findByUserId(userId).isPresent()) {
			throw new AppException(ErrorCode.AGREEMENT_ALREADY_EXISTS);
		}

		Agreement agreement = Agreement.builder()
			.userId(userId)
			.personalInfoAgreement(personalInfoAgreement)
			.privacyPolicyAgreement(privacyPolicyAgreement)
			.termsOfServiceAgreement(termsOfServiceAgreement)
			.marketingAgreement(marketingAgreement)
			.build();

		agreementRepository.save(agreement);
		return agreement.getId();
	}
}