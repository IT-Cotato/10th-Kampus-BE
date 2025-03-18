package com.cotato.kampus.domain.user.dto.response;

import java.util.Map;

public record UnivCertResponse(
	boolean success,
	String universityName,
	String certifiedEmail
) {
	public static UnivCertResponse from(Map<String, Object> response) {
		boolean success = (boolean)response.get("success");
		String universityName = (String)response.get("univName");
		String certifiedEmail = (String)response.get("certified_email");

		return new UnivCertResponse(success, universityName, certifiedEmail);
	}
}