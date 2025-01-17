package com.cotato.kampus.domain.comment.dto.request;

import com.cotato.kampus.domain.common.enums.Anonymity;

public record CommentCreateRequest(
	String content,
	Anonymity anonymity,
	Long parentId
) {
}
