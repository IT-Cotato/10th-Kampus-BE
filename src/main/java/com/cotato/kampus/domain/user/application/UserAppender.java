package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.UserRole;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAppender {

	private final UserRepository userRepository;

	@Transactional
	public Long appendUser(String email, String password, String username, String nickname, String nationality) {
		User user = User.builder()
			.username(username)
			.password(password)
			.email(email)
			.nickname(nickname)
			.nationality(Nationality.valueOf(nationality))
			.userRole(UserRole.UNVERIFIED)
			.build();
		log.info("user email: {}", user.getEmail());

		return userRepository.save(user).getId();
	}
}
