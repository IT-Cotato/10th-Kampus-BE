package com.cotato.kampus.domain.comment.dto.response;

import java.util.List;

import com.cotato.kampus.domain.comment.dto.CommentDetail;

public record CommentListResponse(
	List<CommentDetail> comments) {

	public static CommentListResponse from(List<CommentDetail> comments) {
		return new CommentListResponse(comments);
	}
}