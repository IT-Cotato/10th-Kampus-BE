package com.cotato.kampus.domain.auth.application;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.user.application.UserAppender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AuthService {

	private final UserAppender userAppender;

	public Long signup(String email, String uniqueId, String providerId, String username,
		String nickname, String nationality, String languageCode) {
		return userAppender.appendUser(
			email,
			uniqueId,
			providerId,
			username,
			nickname,
			nationality,
			languageCode
		);
	}
}
