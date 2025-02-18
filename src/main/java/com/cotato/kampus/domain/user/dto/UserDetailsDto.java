package com.cotato.kampus.domain.user.dto;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserStatus;

public record UserDetailsDto(
	Long id,
	String nickname,
	Nationality nationality,
	PreferredLanguage preferredLanguage,
	UserStatus userStatus,
	Long universityId,
	String universityName
) {

	public static UserDetailsDto of(UserDto user, Long universityId, String universityName) {
		return new UserDetailsDto(
			user.id(),
			user.nickname(),
			user.nationality(),
			user.preferredLanguage(),
			user.userStatus(),
			universityId,
			universityName
		);
	}
}