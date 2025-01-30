package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;

public record PostDraftWithPhoto(
	Long draftId,
	String title,
	String content,
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static PostDraftWithPhoto from(PostDraft postDraft, PostDraftPhoto postDraftPhoto) {
		return new PostDraftWithPhoto(
			postDraft.getId(),
			postDraft.getTitle(),
			postDraft.getContent(),
			postDraft.getCreatedTime(),
			postDraftPhoto != null ? postDraftPhoto.getPhotoUrl() : null
		);
	}
}
