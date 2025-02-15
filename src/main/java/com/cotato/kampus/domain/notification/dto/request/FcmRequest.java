package com.cotato.kampus.domain.notification.dto.request;

// Service 계층에서 정보를 전달하기 위한 DTO
public record FcmRequest(
	String deviceToken,
	String title,
	String body
) {
	public static FcmRequest of(String deviceToken, String title, String body){
		return new FcmRequest(
			deviceToken,
			title,
			body
		);
	}
}
