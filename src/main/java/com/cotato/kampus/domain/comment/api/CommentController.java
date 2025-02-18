package com.cotato.kampus.domain.comment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.comment.application.CommentService;
import com.cotato.kampus.domain.comment.dto.request.CommentCreateRequest;
import com.cotato.kampus.domain.comment.dto.response.CommentCreateResponse;
import com.cotato.kampus.domain.comment.dto.response.CommentDeleteResponse;
import com.cotato.kampus.domain.comment.dto.response.CommentLikeResponse;
import com.cotato.kampus.domain.comment.dto.response.CommentListResponse;
import com.cotato.kampus.domain.comment.dto.response.MyCommentResponse;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "댓글(Comment) API", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/posts/{postId}/comments")
	@Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 추가합니다. 대댓글일 경우 parentId에 원래 댓글의 id를 넣어주세요. (기본값 = null)")
	public ResponseEntity<DataResponse<CommentCreateResponse>> createComment(
		@PathVariable Long postId,
		@RequestBody CommentCreateRequest request){

		return ResponseEntity.ok(DataResponse.from(
				CommentCreateResponse.of(
					commentService.createComment(
						postId,
						request.content(),
						request.parentId()
					)
				)
			)
		);
	}

	@DeleteMapping("/comments/{commentId}")
	@Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
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
	@Operation(summary = "댓글 좋아요 추가", description = "특정 댓글에 좋아요를 추가합니다.")
	public ResponseEntity<DataResponse<CommentLikeResponse>> toggleLikeForComment(
		@PathVariable Long commentId
	){

		return ResponseEntity.ok(DataResponse.from(
				CommentLikeResponse.of(
					commentService.likeComment(
						commentId
					)
				)
			)
		);
	}

	@GetMapping("/posts/{postId}/comments")
	@Operation(summary ="댓글 조회", description = "특정 게시글의 모든 댓글과 대댓글을 조회합니다.")
	public ResponseEntity<DataResponse<CommentListResponse>> getPostComments(
		@PathVariable Long postId
	){

			return ResponseEntity.ok(DataResponse.from(
				CommentListResponse.from(
					commentService.findAllCommentsForPost(
						postId
					)
				)
			)
		);
	}

	@GetMapping("/comments/my")
	@Operation(summary = "내가 쓴 댓글 조회", description = "현재 사용자가 작성한 댓글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<MyCommentResponse>> getMyComments(
		@RequestParam(required = false, defaultValue = "0") int page
	){
		return ResponseEntity.ok(DataResponse.from(
				MyCommentResponse.from(
					commentService.findUserComments(page)
				)
			)
		);
	}
}