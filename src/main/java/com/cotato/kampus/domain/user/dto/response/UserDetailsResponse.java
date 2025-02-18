package com.cotato.kampus.domain.user.dto.response;

import com.cotato.kampus.domain.user.dto.UserDetailsDto;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserStatus;

public record UserDetailsResponse(
	Long id,
	String nickname,
	Nationality nationality,
	PreferredLanguage preferredLanguage,
	Long universityId,
	String universityName,
	boolean needSetup
) {
	public static UserDetailsResponse from(UserDetailsDto userDetails) {
		return new UserDetailsResponse(
			userDetails.id(),
			userDetails.nickname(),
			userDetails.nationality(),
			userDetails.preferredLanguage(),
			userDetails.universityId(),
			userDetails.universityName(),
			userDetails.userStatus().equals(UserStatus.PENDING_DETAILS)
		);
	}
}