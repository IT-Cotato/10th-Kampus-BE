package com.cotato.kampus.domain.comment.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.comment.domain.Comment;

public record CommentSummary(
	Long commentId,
	String content,
	Long likes,
	LocalDateTime createdTime
) {
	public static CommentSummary from(Comment comment) {
		return new CommentSummary(
			comment.getId(),
			comment.getContent(),
			comment.getLikes(),
			comment.getCreatedTime()
		);
	}
}
