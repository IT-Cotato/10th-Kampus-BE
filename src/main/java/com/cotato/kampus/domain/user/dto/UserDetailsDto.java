package com.cotato.kampus.domain.user.dto;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

public record UserDetailsDto(
	Long id,
	String nickname,
	Nationality nationality,
	PreferredLanguage preferredLanguage
) {
	public static UserDetailsDto from(User user) {
		return new UserDetailsDto(
			user.getId(),
			user.getNickname(),
			user.getNationality(),
			user.getPreferredLanguage()
		);
	}
}