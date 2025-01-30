package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostDraftDto(
	Long draftId,
	Long userId,
	Long boardId,
	String title,
	String content,
	PostCategory postCategory
) {
	public static PostDraftDto from(PostDraft postDraft){
		return new PostDraftDto(
			postDraft.getId(),
			postDraft.getUserId(),
			postDraft.getBoardId(),
			postDraft.getTitle(),
			postDraft.getContent(),
			postDraft.getPostCategory()
		);
	}
}

