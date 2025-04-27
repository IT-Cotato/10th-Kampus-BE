package com.cotato.kampus.domain.post.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.post.application.PostService;
import com.cotato.kampus.domain.post.api.request.PostCreateRequest;
import com.cotato.kampus.domain.post.api.request.PostUpdateRequest;
import com.cotato.kampus.domain.post.api.response.PostCreateResponse;
import com.cotato.kampus.domain.post.api.response.PostDeleteResponse;
import com.cotato.kampus.domain.post.api.response.PostDetailResponse;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 BoardEntity)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
@RequestMapping("/v1/api/posts")
public class PostController {

	private final PostService postService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 생성", description = "게시글 생성 요청입니다.")
	public ResponseEntity<DataResponse<PostCreateResponse>> createPost(
		@Parameter(description = "Post creation request")
		@Valid @ModelAttribute PostCreateRequest request) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
				PostCreateResponse.of(
					postService.createPost(
						request.boardId(),
						request.title(),
						request.content(),
						request.images(),
						request.categories()
					)
				)
			)
		);
	}

	@GetMapping("/boards/{boardId}")
	@Operation(summary = "게시판의 게시글 리스트 조회",
		description = "게시글을 정렬 기준과 카테고리 기준에 따라 조회합니다.(정렬 기본값: 최신순, 카테고리 기본값: 전체, 페이지당 게시글 수: 10)")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnail>>> findPosts(
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "1") int page,
		@Parameter(
			name = "sort",
			description = "정렬 기준 (recent: 최신순, old: 오래된순, likeCount: 좋아요순)",
			schema = @Schema(type = "string", defaultValue = "recent",
				allowableValues = {"recent", "old", "likeCount"})
		)
		@RequestParam(required = false, defaultValue = "recent") PostSortType sort,
		@Parameter(
			name = "category",
			description = "카테고리명 (전체 조회: 파라미터 미입력, 특정 카테고리 조회: 해당 카테고리명)"
		)
		@RequestParam(required = false) String category

	) {
		return ResponseEntity.ok(
			DataResponse.from(
				SliceResponse.from(
					postService.findPosts(boardId, page, sort, category)
				)
			)
		);
	}

	@GetMapping("/trending")
	@Operation(summary = "트렌딩 게시판의 게시글 목록 조회", description = "트렌딩 게시판의 전체 게시글을 조회합니다.")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnailWithBoardName>>> findTrendingPosts(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(
			DataResponse.from(
				SliceResponse.from(
					postService.findTrendingPosts(page)
				)
			)
		);
	}

	@GetMapping("{postId}")
	@Operation(summary = "게시글 상세 조회", description = "게시글을 세부 내역을 조회합니다.")
	public ResponseEntity<DataResponse<PostDetailResponse>> findPostDetail(
		@PathVariable Long postId) {
		return ResponseEntity.ok(
			DataResponse.from(
				PostDetailResponse.from(
					postService.findPostDetail(postId)
				)
			)
		);
	}

	@DeleteMapping("/{postId}")
	@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
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

	@PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 수정", description = "게시글을 수정합니다. 사진, 카테고리가 없는 경우 빈 값('')을 보내지 말고, 해당 필드를 생략하거나 값을 보내지 않도록 해주세요.")
	public ResponseEntity<DataResponse<Void>> updatePost(
		@PathVariable Long postId,
		@Valid @ModelAttribute PostUpdateRequest request
	) throws ImageException {
		postService.updatePost(
			postId,
			request.title(),
			request.content(),
			request.categories(),
			request.images());

		return ResponseEntity.ok(DataResponse.ok());
	}
}