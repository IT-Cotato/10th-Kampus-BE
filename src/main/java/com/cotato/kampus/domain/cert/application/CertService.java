package com.cotato.kampus.domain.cert.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.enums.UnivMail;
import com.cotato.kampus.domain.cert.implement.CertFinder;
import com.cotato.kampus.domain.cert.implement.CertMailSender;
import com.cotato.kampus.domain.cert.implement.CertManager;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertService {

	private final CertFinder certFinder;
	private final ApiUserResolver apiUserResolver;
	private final UnivFinder univFinder;
	private final CertManager certManager;
	private final CertMailSender certMailSender;

	public boolean checkUnivCode(String univCode) {
		return UnivMail.exists(univCode);
	}

	@Transactional
	public void sendMail(String univCode, String email) {
		// 대학 이름 유효성 검사 및 도메인 검증
		UnivMail.validateUnivCode(univCode);
		boolean domainMatched = UnivMail.getDomains(univCode).stream()
				.anyMatch(email::contains);
		if(!domainMatched) {
			throw new AppException(ErrorCode.INVALID_UNIVERSITY_EMAIL_DOMAIN);
		}

		// 인증 코드 생성
		String code = String.format("%04d", (int)(Math.random() * 10000));

		// 기존 인증 여부 확인 후 처리
		Cert cert = certFinder.findOptionalByEmail(email);
		if(cert != null) {
			// 기존 인증 정보가 있으면 상태 확인 + 코드 갱신
			cert.validateNotCertified();
			certManager.updateCodeAndExpiration(cert, code);
		} else {
			// 기존 인증 정보가 없으면 새로 생성
			Long userId = apiUserResolver.getCurrentUserId();
			String univName = univFinder.findNameByCode(univCode);
			certManager.append(email, univName, code, userId);
		}

		// 메일 발송
		certMailSender.sendVerificationMail(email, code);
	}
}
