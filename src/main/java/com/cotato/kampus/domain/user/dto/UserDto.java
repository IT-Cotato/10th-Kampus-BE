package com.cotato.kampus.domain.user.dto;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

public record UserDto(
	Long id,
	String email,
	String uniqueId,
	String providerId,
	String username,
	String nickname,
	Long universityId,
	String profileImage,
	PreferredLanguage preferredLanguage,
	String deviceToken,
	UserRole userRole,
	UserStatus userStatus
) {
	public static UserDto from(User user) {
		return new UserDto(
			user.getId(),
			user.getEmail(),
			user.getUniqueId(),
			user.getProviderId(),
			user.getUsername(),
			user.getNickname(),
			user.getUniversityId(),
			user.getProfileImage(),
			user.getPreferredLanguage(),
			user.getDeviceToken(),
			user.getUserRole(),
			user.getUserStatus()
		);
	}
}
