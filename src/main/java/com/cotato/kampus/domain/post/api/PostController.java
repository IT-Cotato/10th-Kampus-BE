package com.cotato.kampus.domain.post.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.application.PostService;
import com.cotato.kampus.domain.post.dto.request.PostCreateRequest;
import com.cotato.kampus.domain.post.dto.response.PostCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostDeleteResponse;
import com.cotato.kampus.domain.post.dto.response.PostSliceFindResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/posts")
public class PostController {

	private final PostService postService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DataResponse<PostCreateResponse>> createPost(
		@Parameter(description = "Post creation request")
		@ModelAttribute PostCreateRequest request) throws ImageException {
		return ResponseEntity.ok(DataResponse.from(
			PostCreateResponse.of(
				postService.createPost(
					request.boardId(),
					request.title(),
					request.content(),
					request.postCategory(),
					request.anonymity(),
					request.images()
				)
			)
		));
	}

	@GetMapping("{boardId}")
	@Operation(summary = "게시판의 게시글 리스트 조회", description = "BoardId에 해당하는 전체 게시글을 최신순으로 정렬한 후 슬라이싱 하여 조회합니다.(슬라이스 별 기본 게시글 수: 10)")
	public ResponseEntity<DataResponse<PostSliceFindResponse>> findPosts(
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "0") int page) {
		return ResponseEntity.ok(
			DataResponse.from(
				PostSliceFindResponse.from(
					postService.findPosts(boardId, page)
				)
			)
		);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<PostDeleteResponse>> deletePost(
		@PathVariable Long postId
	) {
		return ResponseEntity.ok(DataResponse.from(
			PostDeleteResponse.of(
				postService.deletePost(
					postId
				)
			)
		));
	}
}
