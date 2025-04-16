package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.enums.Anonymity;
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
		Anonymity anonymity,
		PostStatus postStatus,
		PostType postType
	) {
		super(id, boardId, userId, title, null, anonymity, postStatus, postType);
		validate();
	}

	@Override
	public Post withUpdateInfo(String title, String content) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(title)
			.anonymity(getAnonymity())
			.postStatus(this.getPostStatus())
			.postType(this.getPostType())
			.build();
	}

	@Override
	public Post withPostStatus(PostStatus postStatus) {
		return CardNewsPost.builder()
			.id(this.getId())
			.boardId(this.getBoardId())
			.userId(this.getUserId())
			.title(this.getTitle())
			.anonymity(getAnonymity())
			.postStatus(postStatus)
			.postType(this.getPostType())
			.build();
	}
}
