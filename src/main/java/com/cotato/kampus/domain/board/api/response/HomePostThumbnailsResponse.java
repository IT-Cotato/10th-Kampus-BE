package com.cotato.kampus.domain.board.api.response;

import java.util.List;

import com.cotato.kampus.domain.board.domain.HomePostThumbnail;

public record HomePostThumbnailsResponse(
	List<HomePostThumbnail> previewList
) {
	public static HomePostThumbnailsResponse from(List<HomePostThumbnail> previewList) {
		return new HomePostThumbnailsResponse(
			previewList
		);
	}
}
