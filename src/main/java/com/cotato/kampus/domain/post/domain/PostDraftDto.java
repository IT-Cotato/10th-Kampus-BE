package com.cotato.kampus.domain.post.domain;

public record PostDraftDto(
	Long draftId,
	Long userId,
	Long boardId,
	String title,
	String content
) {
	public static PostDraftDto from(PostDraft postDraft){
		return new PostDraftDto(
			postDraft.getId(),
			postDraft.getUserId(),
			postDraft.getBoardId(),
			postDraft.getTitle(),
			postDraft.getContent()
		);
	}
}

