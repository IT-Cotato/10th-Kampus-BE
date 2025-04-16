package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalPost extends Post{

	@Builder
	public NormalPost(
		Long id,
		Long boardId,
		Long userId,
		String title,
		String content,
		Anonymity anonymity,
		PostStatus postStatus,
		PostType postType
	) {
		super(id, boardId, userId, title, content, anonymity, postStatus, postType);
		validate();
	}

	@Override
	protected void validateForPublishing() {
		super.validateForPublishing();
		validateContent();
	}

	private void validateContent() {
		if(getContent() == null || getContent().trim().isEmpty()) {
			throw new AppException(ErrorCode.POST_CONTENT_EMPTY);
		}
		if(getContent().length() > 1000) {
			throw new AppException(ErrorCode.POST_CONTENT_TOO_LONG);
		}
	}

	@Override
	public Post withUpdateInfo(String title, String content) {
		return NormalPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(title)
			.content(content)
			.anonymity(getAnonymity())
			.postStatus(getPostStatus())
			.postType(getPostType())
			.build();
	}

	@Override
	public Post withPostStatus(PostStatus postStatus) {
		return NormalPost.builder()
			.id(getId())
			.boardId(getBoardId())
			.userId(getUserId())
			.title(getTitle())
			.content(getContent())
			.anonymity(getAnonymity())
			.postStatus(postStatus)
			.postType(getPostType())
			.build();
	}
}
