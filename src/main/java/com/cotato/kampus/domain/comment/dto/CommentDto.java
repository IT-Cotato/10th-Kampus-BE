package com.cotato.kampus.domain.comment.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.comment.enums.ReportStatus;
import com.cotato.kampus.domain.common.enums.Anonymity;

public record CommentDto (
	Long id,
	Long userId,
	Long postId,
	String content,
	Long likes,
	ReportStatus reportStatus,
	CommentStatus commentStatus,
	Anonymity anonymity,
	Long reports,
	Long anonymousNumber,
	Long parentId,
	LocalDateTime createdTime
) {
	public static CommentDto from(Comment comment) {
		return new CommentDto(
			comment.getId(),
			comment.getUserId(),
			comment.getPostId(),
			comment.getContent(),
			comment.getLikes(),
			comment.getReportStatus(),
			comment.getCommentStatus(),
			comment.getAnonymity(),
			comment.getReports(),
			comment.getAnonymousNumber(),
			comment.getParentId(),
			comment.getCreatedTime()
		);
	}
}

