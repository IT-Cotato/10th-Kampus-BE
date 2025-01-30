package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;

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

	public User findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}

	public Boolean existsByNickname(String nickname) {
		return userRepository.existsByNickname(nickname);
	}
}
