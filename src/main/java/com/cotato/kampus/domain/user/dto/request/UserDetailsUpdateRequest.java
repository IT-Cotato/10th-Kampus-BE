package com.cotato.kampus.domain.user.dto.request;

import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;

public record UserDetailsUpdateRequest(
	String nickname,
	Nationality nationality,
	PreferredLanguage preferredLanguage
) {
}