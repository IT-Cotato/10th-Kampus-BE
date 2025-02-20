package com.cotato.kampus.domain.admin.dto.response;

import java.util.List;

import com.cotato.kampus.domain.admin.dto.AdminBoardDetail;

public record AdminBoardListResponse(
	List<AdminBoardDetail> adminBoardDetails
) {
	public static AdminBoardListResponse from(List<AdminBoardDetail> adminBoardDetail) {
		return new AdminBoardListResponse(adminBoardDetail);
	}
}
