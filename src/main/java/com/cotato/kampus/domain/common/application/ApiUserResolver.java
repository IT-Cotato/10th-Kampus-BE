package com.cotato.kampus.domain.common.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.user.application.UserFinder;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.auth.nativeapp.AppUserDetails;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiUserResolver {

	private final UserFinder userFinder;

	public User getCurrentUser(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof AppUserDetails userDetails) {
			log.info("UniqueId from AppUserDetails: {}", userDetails.getUniqueId());
			return userFinder.findByUniqueId(userDetails.getUniqueId());
		} else {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
	}

	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

	public UserDto getCurrentUserDto() {
		User user = getCurrentUser();
		return UserDto.from(user);
	}
}
