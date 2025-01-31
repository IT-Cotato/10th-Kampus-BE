package com.cotato.kampus.domain.admin.dto.response;

public record BoardCreateResponse(
	Long boardId
) {
	public static BoardCreateResponse from(Long boardId) {
		return new BoardCreateResponse(boardId);
	}
}
