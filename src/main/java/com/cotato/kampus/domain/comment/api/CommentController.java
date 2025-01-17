package com.cotato.kampus.domain.comment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.comment.application.CommentService;
import com.cotato.kampus.domain.comment.dto.request.CommentCreateRequest;
import com.cotato.kampus.domain.comment.dto.response.CommentCreateResponse;
import com.cotato.kampus.domain.comment.dto.response.CommentDeleteResponse;
import com.cotato.kampus.domain.comment.dto.response.CommentLikeResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/posts/{postId}/comments")
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

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<DataResponse<CommentDeleteResponse>> deleteComment(
		@PathVariable Long commentId
	){

		return ResponseEntity.ok(DataResponse.from(
				CommentDeleteResponse.of(
					commentService.deleteComment(
						commentId
					)
				)
			)
		);
	}

	@PostMapping("/comments/{commentId}/like")
	public ResponseEntity<DataResponse<CommentLikeResponse>> toggleLikeForComment(
		@PathVariable Long commentId
	){

		return ResponseEntity.ok(DataResponse.from(
				CommentLikeResponse.of(
					commentService.likeComment(commentId)
				)
			)
		);
	}
}
