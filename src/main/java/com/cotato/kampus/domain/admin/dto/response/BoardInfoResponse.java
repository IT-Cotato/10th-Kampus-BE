package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.admin.dto.BoardDetails;

public record BoardInfoResponse(
	BoardDetails boardDetails
) {
	public static BoardInfoResponse from(BoardDetails boardDetails) {
		return new BoardInfoResponse(
			boardDetails
		);
	}
}