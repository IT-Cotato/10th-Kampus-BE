package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostPhoto{

	private final Long id;
	private final Long postId;
	private final String photoUrl;
	private final int order;

	@Builder
	public PostPhoto(Long id, Long postId, String photoUrl, int order) {
		this.id = id;
		this.postId = postId;
		this.photoUrl = photoUrl;
		this.order = order;
		validate();
	}

	private void validate() {
		if (postId == null) {
			throw new AppException(ErrorCode.POST_PHOTO_POST_ID_REQUIRED);
		}
		if (photoUrl == null) {
			throw new AppException(ErrorCode.POST_PHOTO_URL_REQUIRED);
		}
	}
}
