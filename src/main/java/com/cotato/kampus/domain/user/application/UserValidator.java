package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserValidator {
	private final ApiUserResolver apiUserResolver;
	private final UserFinder userFinder;

	// 재학생 인증 후 universityId 반환
	public Long validateStudentVerification(Long userId) {
		User user = userFinder.findById(userId);

		if (user.getUserRole() == UserRole.UNVERIFIED)
			throw new AppException(ErrorCode.USER_UNVERIFIED);

		return user.getUniversityId();
	}

	// 재학생 인증 중복 요청 검증
	public Long validateDuplicateStudentVerification(){
		User user = apiUserResolver.getUser();

		if(user.getUserRole() == UserRole.VERIFIED)
			throw new AppException(ErrorCode.USER_ALREADY_VERIFIED);

		return user.getId();
	}

	public void validateAdminAccess() {
		User user = apiUserResolver.getUser();

		if(user.getUserRole() != UserRole.ADMIN) {
			throw new AppException(ErrorCode.USER_NOT_ADMIN);
		}
	}

	public void validateUserDetailsUpdate(String nickname) {
		validateUserStatus();
		validateDuplicatedNickname(nickname);
	}

	private void validateUserStatus() {
		User user = apiUserResolver.getUser();
		if (user.getUserStatus() == UserStatus.ACTIVE) {
			throw new AppException(ErrorCode.USER_ALREADY_REGISTERED);
		}
	}

	private void validateDuplicatedNickname(String nickname) {
		if (userFinder.existsByNickname(nickname)) {
			throw new AppException(ErrorCode.USER_NICKNAME_DUPLICATED);
		}
	}
}
