package com.cotato.kampus.domain.notification.dto;

// Service 계층에서 정보를 전달하기 위한 DTO
public record FcmRequestDto(
	String deviceToken,
	String title,
	String body
) {
	public static FcmRequestDto of(String deviceToken, String title, String body){
		return new FcmRequestDto(
			deviceToken,
			title,
			body
		);
	}
}
