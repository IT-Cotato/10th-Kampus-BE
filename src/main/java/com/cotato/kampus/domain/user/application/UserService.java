package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserService {

	private final UserUpdater userUpdater;
	private final UserValidator userValidator;
	private final ApiUserResolver apiUserResolver;

	public UserDetailsDto getUserDetails() {
		return UserDetailsDto.from(apiUserResolver.getUser());
	}

	@Transactional
	public Long updateUserDetails(String nickname, Nationality nationality, PreferredLanguage preferredLanguage) {
		userValidator.validateDuplicatedNickname(nickname);
		return userUpdater.updateDetails(nickname, nationality, preferredLanguage);
	}
}