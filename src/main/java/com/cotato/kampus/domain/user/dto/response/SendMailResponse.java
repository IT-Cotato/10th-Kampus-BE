package com.cotato.kampus.domain.user.dto.response;

import java.util.Map;

public record SendMailResponse(
	Map<String, Object> result
) {
	public static SendMailResponse from(Map<String, Object> result) {
		return new SendMailResponse(result);
	}
}
