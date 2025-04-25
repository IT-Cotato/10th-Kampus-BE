package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import com.cotato.kampus.domain.post.domain.TempPhotoInfo;
import com.cotato.kampus.domain.post.domain.TempPostDetails;

public record TempPostDetailResponse(
	Long draftId,
	Long boardId,
	String title,
	String content,
	List<TempPhotoInfo> postPhotos
) {
	public static TempPostDetailResponse from(TempPostDetails tempPostDetails) {
		return new TempPostDetailResponse(
			tempPostDetails.tempPostId(),
			tempPostDetails.boardId(),
			tempPostDetails.title(),
			tempPostDetails.content(),
			tempPostDetails.postPhotos()
		);
	}
}
