package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDraftWithPhoto(
	Long draftId,
	PostCategory postCategory,
	String title,
	String content,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl
) {
	public static PostDraftWithPhoto from(PostDraft postDraft, PostDraftPhoto postDraftPhoto) {
		return new PostDraftWithPhoto(
			postDraft.getId(),
			postDraft.getPostCategory(),
			postDraft.getTitle(),
			postDraft.getContent(),
			postDraft.getCreatedTime(),
			postDraftPhoto != null ? postDraftPhoto.getPhotoUrl() : null
		);
	}
}
