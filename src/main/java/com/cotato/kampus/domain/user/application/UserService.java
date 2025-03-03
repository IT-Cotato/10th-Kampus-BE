package com.cotato.kampus.domain.user.application;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.university.application.UnivEmailVerifier;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.verification.application.VerificationPhotoAppender;
import com.cotato.kampus.domain.verification.application.VerificationRecordAppender;
import com.cotato.kampus.domain.verification.application.VerificationRecordFinder;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserService {

	private final UserUpdater userUpdater;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;
	private final AgreementAppender agreementAppender;
	private final VerificationRecordAppender verificationRecordAppender;

	private final UnivEmailVerifier univEmailVerifier;
	private final UnivFinder univFinder;
	private final S3Uploader s3Uploader;
	private final VerificationPhotoAppender verificationPhotoAppender;

	private static final String STUDENT_CERT_IMAGE_FOLDER = "student_cert";
	private final VerificationRecordFinder verificationRecordFinder;

	public UserDetailsDto getUserDetails() {
		UserDto user = apiUserResolver.getCurrentUserDto();
		return Optional.ofNullable(user.universityId())
			.map(id -> UserDetailsDto.of(
				user,
				id,
				univFinder.findUniversity(id).getUniversityName()
			))
			.orElse(UserDetailsDto.of(user, -1L, ""));
	}

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
	public Map<String, Object> sendMail(String email, String universityName) throws IOException {
		return univEmailVerifier.sendMail(email, universityName);
	}

	@Transactional
	public Long verifyEmailCode(String email, String universityName, int code) throws IOException {
		// 이미 재학생 인증 되었는지 확인
		userValidator.validateDuplicateStudentVerification();

		// 코드 인증
		univEmailVerifier.verifyCode(email, universityName, code);

		// VerificationRecord 추가
		Long universityId = univFinder.findUniversityId(universityName);
		verificationRecordAppender.appendEmailType(universityId);

		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 유저 상태 변경, 학교 할당
		return userUpdater.updateVerificationStatus(userId, universityId);
	}

	@Transactional
	public void uploadCert(String universityName, MultipartFile certImage) throws ImageException {
		// 이미 재학생 인증 되었는지 검증
		Long userId = userValidator.validateDuplicateStudentVerification();

		// s3에 이미지 업로드
		String imageUrl = s3Uploader.uploadFile(certImage, STUDENT_CERT_IMAGE_FOLDER);

		// VerificationRecord 추가
		Long universityId = univFinder.findUniversityId(universityName);
		Long verificationRecordId = verificationRecordAppender.appendPhotoType(userId, universityId);

		// 인증서 이미지 추가
		verificationPhotoAppender.append(verificationRecordId, imageUrl);
	}

	// 닉네임 가능하면 true
	public Boolean checkNicknameAvailability(String nickname) {
		return userValidator.checkNicknameAvailability(nickname);
	}

	@Transactional
	public Long updateUserInfo(String nickname, PreferredLanguage preferredLanguage) {
		// 닉네임 검증
		userValidator.validateUserInfoUpdate(nickname);
		// 2. 유저 닉네임, 선호언어 설정
		return userUpdater.updateDetails(nickname, preferredLanguage);
	}

	public VerificationStatus findVerifyStatus() {
		// 유저 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		// 재학생 인증 기록 조회
		VerificationRecordDto record = verificationRecordFinder.findByUserId(userDto.id());

		return record.verificationStatus();
	}
}