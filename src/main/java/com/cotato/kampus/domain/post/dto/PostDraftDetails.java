package com.cotato.kampus.domain.post.dto;

import java.util.List;

import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostDraftDetails(
	Long draftId,
	Long boardId,
	String title,
	String content,
	PostCategory postCategory,
	List<String> postPhotos
) {
	public static PostDraftDetails of(PostDraftDto postDraftDto, List<String> postPhotos) {
		return new PostDraftDetails(
			postDraftDto.draftId(),
			postDraftDto.boardId(),
			postDraftDto.title(),
			postDraftDto.content(),
			postDraftDto.postCategory(),
			postPhotos
		);
	}
}
