package com.cotato.kampus.domain.comment.enums;

public enum CommentStatus {
	NORMAL,
	MASKED, // 마스킹 표시 (대댓글 떄문에 남겨둠)
	REMOVED, // 완전 제거 (조회에서 제외)
	MASKED_BY_ADMIN,
	REMOVED_BY_ADMIN
}