package com.cotato.kampus.domain.board.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.HomeBoardAndPostPreview;

public record HomeBoardAndPostPreviewResponse(
	List<HomeBoardAndPostPreview> previewList
) {
	public static HomeBoardAndPostPreviewResponse from(List<HomeBoardAndPostPreview> previewList) {
		return new HomeBoardAndPostPreviewResponse(
			previewList
		);
	}
}
