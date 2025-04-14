package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDraftWithPhoto(
	Long draftId,
	Long boardId,
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static PostDraftWithPhoto from(PostDraft postDraft, PostDraftPhoto postDraftPhoto) {
		return new PostDraftWithPhoto(
			postDraft.getId(),
			postDraft.getBoardId(),
			postDraft.getTitle(),
			postDraft.getContent(),
			postDraft.getCreatedTime(),
			postDraftPhoto != null ? postDraftPhoto.getPhotoUrl() : null
		);
	}
}
