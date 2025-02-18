package com.cotato.kampus.domain.comment.dto.response;

import java.util.List;

import com.cotato.kampus.domain.comment.dto.CommentDetail;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentListResponse(
	@ArraySchema(schema = @Schema(implementation = CommentDetail.class))
	List<CommentDetail> comments) {

	public static CommentListResponse from(List<CommentDetail> comments) {
		return new CommentListResponse(comments);
	}
}