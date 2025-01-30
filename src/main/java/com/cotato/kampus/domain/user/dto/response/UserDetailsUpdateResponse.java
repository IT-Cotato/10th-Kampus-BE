package com.cotato.kampus.domain.user.dto.response;

public record UserDetailsUpdateResponse(
	Long userId
) {
	public static UserDetailsUpdateResponse from(Long id) {
		return new UserDetailsUpdateResponse(id);
	}
}