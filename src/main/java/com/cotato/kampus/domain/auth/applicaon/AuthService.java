package com.cotato.kampus.domain.auth.applicaon;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.user.application.UserAppender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AuthService {

	private final UserAppender userAppender;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public Long signup(String email, String uniqueId, String providerId, String password, String username,
		String nickname, String nationality) {
		return userAppender.appendUser(
			email,
			uniqueId,
			providerId,
			bCryptPasswordEncoder.encode(password),
			username,
			nickname,
			nationality
		);
	}
}
