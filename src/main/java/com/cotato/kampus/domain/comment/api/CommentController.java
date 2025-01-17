package com.cotato.kampus.domain.comment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.comment.application.CommentService;
import com.cotato.kampus.domain.comment.dto.request.CommentCreateRequest;
import com.cotato.kampus.domain.comment.dto.response.CommentCreateResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/comments")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{postId}")
	public ResponseEntity<DataResponse<CommentCreateResponse>> createComment(
		@PathVariable Long postId,
		@RequestBody CommentCreateRequest request){

		return ResponseEntity.ok(DataResponse.from(
				CommentCreateResponse.of(
					commentService.createComment(
						postId,
						request.content(),
						request.anonymity(),
						request.parentId()
					)
				)
			)
		);
	}
}
