package com.cotato.kampus.domain.post.api.response;

import java.util.List;
import com.cotato.kampus.domain.post.domain.PostDraftDetails;

public record PostDraftDetailResponse(
	Long draftId,
	Long boardId,
	String title,
	String content,
	List<String> postPhotoUrls
) {
	public static PostDraftDetailResponse from(PostDraftDetails postDraftDetails) {
		return new PostDraftDetailResponse(
			postDraftDetails.draftId(),
			postDraftDetails.boardId(),
			postDraftDetails.title(),
			postDraftDetails.content(),
			postDraftDetails.postPhotos()
		);
	}
}
