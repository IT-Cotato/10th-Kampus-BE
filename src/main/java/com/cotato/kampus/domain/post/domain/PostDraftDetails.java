package com.cotato.kampus.domain.post.domain;

import java.util.List;

public record PostDraftDetails(
	Long draftId,
	Long boardId,
	String title,
	String content,
	List<String> postPhotos
) {
	public static PostDraftDetails of(PostDraftDto postDraftDto, List<String> postPhotos) {
		return new PostDraftDetails(
			postDraftDto.draftId(),
			postDraftDto.boardId(),
			postDraftDto.title(),
			postDraftDto.content(),
			postPhotos
		);
	}
}
