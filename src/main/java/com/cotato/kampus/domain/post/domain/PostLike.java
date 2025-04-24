package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLike {

	private final Long id;
	private final Long userId;
	private final Long postId;

	@Builder
	public PostLike(Long id, Long userId, Long postId) {
		this.id = id;
		this.userId = userId;
		this.postId = postId;
		validate();
	}

	private void validate() {
		if (userId == null) {
			throw new AppException(ErrorCode.POST_LIKE_USER_ID_REQUIRED);
		}
		if (postId == null) {
			throw new AppException(ErrorCode.POST_LIKE_POST_ID_REQUIRED);
		}
	}
}
