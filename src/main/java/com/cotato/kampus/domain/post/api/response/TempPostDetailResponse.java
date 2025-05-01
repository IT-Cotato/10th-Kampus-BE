package com.cotato.kampus.domain.post.api.response;

import com.cotato.kampus.domain.post.domain.TempPostDetails;

public record TempPostDetailResponse(
	TempPostDetails tempPostDetails
) {
	public static TempPostDetailResponse from(TempPostDetails tempPostDetails) {
		return new TempPostDetailResponse(tempPostDetails);
	}
}
