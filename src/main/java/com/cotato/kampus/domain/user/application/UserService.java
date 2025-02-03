package com.cotato.kampus.domain.user.application;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.university.application.UnivEmailVerifier;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserService {

	private final UserUpdater userUpdater;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;
	private final AgreementAppender agreementAppender;

	public UserDetailsDto getUserDetails() {
		return UserDetailsDto.from(apiUserResolver.getUser());
	}
	private final UnivEmailVerifier univEmailVerifier;
	private final UnivFinder univFinder;

	@Transactional
	public Long updateUserDetails(String nickname, Nationality nationality, PreferredLanguage preferredLanguage,
		boolean personalInfoAgreement, boolean privacyPolicyAgreement,
		boolean termsOfServiceAgreement, boolean marketingAgreement) {
		// 1. 세부정보 등록 가능한지 검증 (활성화 상태, 닉네임 중복 검증)
		userValidator.validateUserDetailsUpdate(nickname);
		// 2. 유저 닉네임, 국적, 선호언어 설정
		Long userId = userUpdater.updateDetails(nickname, nationality, preferredLanguage);
		// 3. 유저 동의 내역 추가
		agreementAppender.appendAgreement(userId, personalInfoAgreement, privacyPolicyAgreement,
			termsOfServiceAgreement, marketingAgreement);
		return userId;
	}

	@Transactional
	public Map<String, Object> sendMail(String email, String univName) throws IOException {
		return univEmailVerifier.sendMail(email, univName);
	}

	@Transactional
	public Long verifyEmailCode(String email, String univName, int code) throws IOException {
		// 코드 인증
		univEmailVerifier.verifyCode(email, univName, code);

		// 학교 검색
		Long universityId = univFinder.findUniversityId(univName);

		// 유저 상태 변경, 학교 할당
		return userUpdater.updateVerificationStatus(universityId);
	}
}