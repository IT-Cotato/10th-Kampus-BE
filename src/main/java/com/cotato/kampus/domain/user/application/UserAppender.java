package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.user.dao.UserRepository;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
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
	public Long appendUser(String email, String uniqueId, String providerId, String username,
		String nickname, String nationality, String languageCode) {
		User user = User.builder()
			.username(username)
			.uniqueId(uniqueId)
			.providerId(providerId)
			.email(email)
			.nickname(nickname)
			.nationality(Nationality.valueOf(nationality))
			.preferredLanguage(PreferredLanguage.fromCode(languageCode))
			.userRole(UserRole.UNVERIFIED)
			.build();
		log.info("user uniqueId: {}", user.getUniqueId());

		return userRepository.save(user).getId();
	}
}
