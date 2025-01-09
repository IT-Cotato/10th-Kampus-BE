package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserValidator {
	private final UserFinder userFinder;

	public void validateStudentVerification() {
		User user = userFinder.currentUser();

		if (user.getUserRole() == UserRole.UNVERIFIED)
			throw new AppException(ErrorCode.USER_UNVERIFIED);
	}
}
