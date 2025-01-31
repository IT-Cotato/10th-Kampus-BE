package com.cotato.kampus.domain.admin.dto.response;

import java.util.List;

import com.cotato.kampus.domain.admin.dto.BoardDetail;

public record AdminBoardListResponse(
	List<BoardDetail> boardDetails
) {
	public static AdminBoardListResponse from(List<BoardDetail> boardDetail) {
		return new AdminBoardListResponse(boardDetail);
	}
}
