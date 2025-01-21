package com.cotato.kampus.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.comment.enums.CommentStatus;

public record CommentDetail(
	Long commentId,
	CommentStatus commentStatus,
	String author,
	String content,
	Long likes,
	LocalDateTime createdTime,
	List<CommentDetail> replies
) {
	public static CommentDetail of(CommentDto commentDto, String author, List<CommentDetail> replies) {
		return new CommentDetail(
			commentDto.id(),
			commentDto.commentStatus(),
			author,
			commentDto.content(),
			commentDto.likes(),
			commentDto.createdTime(),
			replies
		);
	}
}