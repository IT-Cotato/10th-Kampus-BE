package com.cotato.kampus.domain.auth.domain;

import lombok.Builder;

@Builder
public record ReissuedToken(
	String accessToken,
	String refreshToken
) {
}
