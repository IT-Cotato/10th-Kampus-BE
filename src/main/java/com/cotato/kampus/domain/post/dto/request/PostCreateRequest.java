package com.cotato.kampus.domain.post.dto.request;

import com.cotato.kampus.domain.common.enums.Anonymity;

public record PostCreateRequest(
	Long boardId,
	String title,
	String content,
	String postCategory,
	Anonymity anonymity
) {
}
