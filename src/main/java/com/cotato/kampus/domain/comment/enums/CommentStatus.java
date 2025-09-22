package com.cotato.kampus.domain.comment.enums;

import java.util.EnumSet;
import java.util.Set;

public enum CommentStatus {
	NORMAL,
	MASKED, // 마스킹 표시 (대댓글 때문에 남겨둠)
	REMOVED, // 완전 제거 (조회에서 제외)
	MASKED_BY_ADMIN,
	REMOVED_BY_ADMIN;

	private static final Set<CommentStatus> VISIBLE_STATUSES = EnumSet.of(NORMAL, MASKED, REMOVED);

	public boolean isRemoved() {
		return this == REMOVED || this == REMOVED_BY_ADMIN;
	}

	public boolean isMasked() {
		return this == MASKED || this == MASKED_BY_ADMIN;
	}

	public boolean isVisible() {
		return this == NORMAL || isMasked();
	}

	public static Set<CommentStatus> getVisibleStatuses() {
		return VISIBLE_STATUSES;
	}
}