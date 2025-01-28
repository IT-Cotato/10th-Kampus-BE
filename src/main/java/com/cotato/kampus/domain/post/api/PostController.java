package com.cotato.kampus.domain.post.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.application.PostService;
import com.cotato.kampus.domain.post.dto.request.PostCreateRequest;
import com.cotato.kampus.domain.post.dto.request.PostDraftRequest;
import com.cotato.kampus.domain.post.dto.request.PostUpdateRequest;
import com.cotato.kampus.domain.post.dto.response.MyPostResponse;
import com.cotato.kampus.domain.post.dto.response.PostCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostDeleteResponse;
import com.cotato.kampus.domain.post.dto.response.PostDetailResponse;
import com.cotato.kampus.domain.post.dto.response.PostDraftSliceFindResponse;
import com.cotato.kampus.domain.post.dto.response.PostDraftCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostSliceFindResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 Board)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/posts")
public class PostController {

	private final PostService postService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 생성", description = "게시글 생성 요청입니다. 사진이 없는 경우 빈 값('')을 보내지 말고, 해당 필드를 생략하거나 값을 보내지 않도록 해주세요.")
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
					request.images() == null ? List.of() : request.images()
				)
			)
		));
	}

	@GetMapping("/boards/{boardId}")
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
	@Operation(summary = "게시글 삭제", description = "(현재 유저가 작성한 게시글일 경우) 게시글을 삭제합니다.")
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
	@Operation(summary = "게시글 수정", description = "게시글의 내용 수정")
	public ResponseEntity<DataResponse<Void>> updatePost(
		@PathVariable Long postId,
		@ModelAttribute PostUpdateRequest request
	) throws ImageException {
		postService.updatePost(postId, request.title(), request.content(), request.postCategory(), request.anonymity(),
			request.images() == null ? List.of() : request.images()); // 이미지 없는 경우 빈 리스트로 요청
		return ResponseEntity.ok(DataResponse.ok());
  	}

	@PostMapping(value = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 임시 저장", description = "게시글을 임시 저장합니다. boardId는 필수 값입니다. 사진이 없는 경우 빈 값('')을 보내지 말고, 해당 필드를 생략하거나 값을 보내지 않도록 해주세요.")
	public ResponseEntity<DataResponse<PostDraftCreateResponse>> draftPost(
		@Parameter(description = "Post creation request")
		@Valid @ModelAttribute PostDraftRequest request) throws ImageException {
		return ResponseEntity.ok(DataResponse.from(
			PostDraftCreateResponse.of(
				postService.draftPost(
					request.boardId(),
					request.title(),
					request.content(),
					request.postCategory(),
					request.images() == null ? List.of() : request.images()
				)
			)
		));
	}

	@GetMapping(value = "/boards/{boardId}/draft")
	@Operation(summary = "임시 저장글 리스트 조회", description = "해당 게시판의 임시 저장글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<PostDraftSliceFindResponse>> findDraftPost(
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "0") int page
	){
			return ResponseEntity.ok(DataResponse.from(
				PostDraftSliceFindResponse.from(
					postService.findPostDrafts(boardId, page)
				)
			)
		);
	}

	@DeleteMapping(value = "/draft/{draftPostId}")
	@Operation(summary = "임시 저장 게시글 삭제", description = "임시 저장글을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteDraftPost(
		@PathVariable Long draftPostId
	){
		postService.deleteDraftPost(draftPostId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my")
	@Operation(summary = "[마이페이지] 내가 쓴 게시글 조회", description = "현재 사용자가 작성한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<MyPostResponse>> findMyPosts(
		@RequestParam(required = false, defaultValue = "0") int page
	){
			return ResponseEntity.ok(DataResponse.from(
				MyPostResponse.from(
					postService.findUserPosts(page)
				)
			)
		);
	}

  @PostMapping("/{postId}/likes")
	@Operation(summary = "게시글 좋아요", description = "게시글 좋아요")
	public ResponseEntity<DataResponse<Void>> likePost(
		@PathVariable Long postId
	) {
		postService.likePost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/{postId}/scrap")
	@Operation(summary = "게시글 스크랩", description = "게시글을 스크랩합니다.")
	public ResponseEntity<DataResponse<Void>> scrapPost(
		@PathVariable Long postId
	){
		postService.scrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{postId}/scrap")
	@Operation(summary = "게시글 스크랩 취소", description = "게시글 스크랩을 해제합니다.")
	public ResponseEntity<DataResponse<Void>> unscrapPost(
		@PathVariable Long postId
	){
		postService.unscrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my/scrap")
	@Operation(summary = "[마이페이지] 스크랩한 게시글 조회", description = "현재 사용자가 스크랩한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<MyPostResponse>> findMyScrapedPosts(
		@RequestParam(required = false, defaultValue = "0") int page
	){
		return ResponseEntity.ok(DataResponse.from(
				MyPostResponse.from(
					postService.findUserScrapedPosts(page)
				)
			)
		);
	}
}