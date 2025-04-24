package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrendingPost{

	private final Long id;
	private final Long postId;

	@Builder
	public TrendingPost(Long id, Long postId) {
		this.id = id;
		this.postId = postId;
		validate();
	}

	private void validate() {
		if (postId == null) {
			throw new AppException(ErrorCode.TRENDING_POST_ID_REQUIRED);
		}
	}
}
