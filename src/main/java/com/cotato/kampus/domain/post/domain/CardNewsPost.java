package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CardNewsPost extends Post {

	@Builder
	public CardNewsPost(
		Long id,
		Long boardId,
		Long userId,
		String title,
		PostStatus postStatus,
		LocalDateTime createdTime,
		LocalDateTime lastModifiedTime
	) {
		super(id, boardId, userId, title, postStatus, createdTime, lastModifiedTime);
		validate();
	}

	public CardNewsPost withUpdateInfo(String title) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(title)
			.postStatus(this.getPostStatus())
			.build();
	}

	public CardNewsPost withPostStatus(PostStatus postStatus) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(this.getTitle())
			.postStatus(postStatus)
			.build();
	}
}
