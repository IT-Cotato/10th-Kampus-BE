package com.cotato.kampus.domain.comment.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.comment.dto.CommentSummary;

public record MyCommentResponse(
	List<CommentSummary> comments,
	boolean hasNext
) {
	public static MyCommentResponse from(Slice<CommentSummary> commentSummarySlice) {
		return new MyCommentResponse(
			commentSummarySlice.getContent(),
			commentSummarySlice.hasNext()
		);
	}
}
