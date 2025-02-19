package com.cotato.kampus.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.comment.enums.CommentStatus;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentDetail(
	@Schema(example = "11")
	Long commentId,

	Long parentId,

	@Schema(example = "NORMAL")
	CommentStatus commentStatus,

	@Schema(example = "Anonymous1")
	String author,

	@Schema(example = "This is a test comment.")
	String content,

	@Schema(example = "3")
	Long likes,

	@Schema(example = "true")
	boolean isLiked,

	@Schema(example = "2025-01-17T21:03:30.218321")
	LocalDateTime createdTime,

	@ArraySchema(schema = @Schema(example = "[{\"commentId\": 14, \"commentStatus\": \"NORMAL\", \"author\": \"Anonymous1\", \"content\": \"This is a reply.\", \"likes\": 0, \"createdTime\": \"2025-01-17T21:04:35.714918\", \"replies\": []}]"))
	@Schema(description = "댓글의 대댓글 리스트")
	List<CommentDetail> replies
) {
	public static CommentDetail of(CommentDto commentDto, String author, List<CommentDetail> replies, boolean isLiked) {
		return new CommentDetail(
			commentDto.commentId(),
			commentDto.parentId(),
			commentDto.commentStatus(),
			author,
			commentDto.content(),
			commentDto.likes(),
			isLiked,
			commentDto.createdTime(),
			replies
		);
	}
}