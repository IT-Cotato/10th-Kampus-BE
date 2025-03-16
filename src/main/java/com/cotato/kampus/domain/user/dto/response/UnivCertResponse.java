package com.cotato.kampus.domain.user.dto.response;

import java.util.Map;

public record UnivCertResponse(
	boolean success,
	Integer status,
	String message
) {
	public static UnivCertResponse from(Map<String, Object> response) {
		boolean success = (boolean) response.get("success");
		Integer status = response.containsKey("status") ? (Integer) response.get("status") : null;
		String message = response.containsKey("message") ? (String) response.get("message") : null;

		return new UnivCertResponse(success, status, message);
	}
}
