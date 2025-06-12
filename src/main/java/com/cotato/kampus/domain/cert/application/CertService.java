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
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.verification.application.VerificationRecordAppender;
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
	private final UserUpdater userUpdater;
	private final UserValidator userValidator;
	private final VerificationRecordAppender verificationRecordAppender;

	public boolean checkUnivCode(String univCode) {
		return UnivMail.exists(univCode);
	}

	@Transactional
	public void sendMail(String univCode, String email) {
		// 유저 조회 및 검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateDuplicateStudentVerification(user);

		// 대학 이름 유효성 검사 및 도메인 검증
		UnivMail.validateUnivCode(univCode);
		boolean domainMatched = UnivMail.getDomains(univCode).stream()
				.anyMatch(email::contains);
		if(!domainMatched) {
			throw new AppException(ErrorCode.INVALID_UNIVERSITY_EMAIL_DOMAIN);
		}

		// 인증 코드 생성
		String code = String.format("%04d", (int)(Math.random() * 10000));

		// 해당 이메일로 인증 요청 여부 확인 후 처리
		Cert cert = certFinder.findOptionalByEmail(email);
		if(cert != null) {
			// 인증 요청 정보가 있으면 상태 확인 + 코드 갱신
			cert.validateNotCertified();
			certManager.updateCodeAndExpiration(cert, code);
		} else {
			// 인증 요청 정보가 없으면 새로 생성
			certManager.append(email, univCode, code, user.id());
		}

		// 메일 발송
		certMailSender.sendVerificationMail(email, code);
	}

	@Transactional
	public void verifyEmailCode(String email, String code) {
		// 유저 조회 및 검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateDuplicateStudentVerification(user);

		// 해당 이메일로 인증 요청 조회/검증 + 업데이트
		Cert cert = certFinder.findByEmail(email);
		cert.validateNotCertified();
		cert.validateExpired();
		cert.validateCode(code);
		certManager.certify(cert);

		// 유저 대학 정보 업데이트
		Long universityId = univFinder.findUniversityId(cert.getUnivCode());
		userUpdater.updateVerificationStatus(user.id(), universityId);

		// 인증 기록 추가
		verificationRecordAppender.appendEmailType(user.id(), universityId);
	}

	@Transactional
	public void clear() {
		UserDto user = apiUserResolver.getCurrentUserDto();
		certManager.deleteAllByUserId(user.id());
		userUpdater.updateRole(user.id(), UserRole.UNVERIFIED);
	}
}

