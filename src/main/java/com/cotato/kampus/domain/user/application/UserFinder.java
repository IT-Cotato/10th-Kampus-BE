package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFinder {

	private final UserRepository userRepository;

	public User findByUniqueId(String uniqueId) {
		return userRepository.findByUniqueId(uniqueId)
			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}

	public User findById(Long id){
		return userRepository.findById(id)
			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}
}
