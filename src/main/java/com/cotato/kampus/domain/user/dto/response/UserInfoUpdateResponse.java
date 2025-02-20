package com.cotato.kampus.domain.user.dto.response;

public record UserInfoUpdateResponse(
	Long userId
) {
	public static UserInfoUpdateResponse from(Long id) {
		return new UserInfoUpdateResponse(id);
	}
}