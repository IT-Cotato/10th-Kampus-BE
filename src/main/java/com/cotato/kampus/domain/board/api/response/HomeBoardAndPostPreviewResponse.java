package com.cotato.kampus.domain.board.api.response;

import java.util.List;

import com.cotato.kampus.domain.board.domain.HomeBoardAndPostPreview;

public record HomeBoardAndPostPreviewResponse(
	List<HomeBoardAndPostPreview> previewList
) {
	public static HomeBoardAndPostPreviewResponse from(List<HomeBoardAndPostPreview> previewList) {
		return new HomeBoardAndPostPreviewResponse(
			previewList
		);
	}
}
