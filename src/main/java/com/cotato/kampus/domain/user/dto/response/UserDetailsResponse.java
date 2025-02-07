package com.cotato.kampus.domain.user.dto.response;

import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

public record UserDetailsResponse(
	Long id,
	String nickname,
	Nationality nationality,
	PreferredLanguage preferredLanguage
) {
	public static UserDetailsResponse from(UserDetailsDto userDetails) {
		return new UserDetailsResponse(
			userDetails.id(),
			userDetails.nickname(),
			userDetails.nationality(),
			userDetails.preferredLanguage()
		);
	}
}