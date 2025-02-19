package com.cotato.kampus.domain.post.api;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.board.application.BoardService;
import com.cotato.kampus.domain.post.application.PostService;
import com.cotato.kampus.domain.post.dto.request.DraftDeleteRequest;
import com.cotato.kampus.domain.post.dto.request.PostCreateRequest;
import com.cotato.kampus.domain.post.dto.request.PostDraftRequest;
import com.cotato.kampus.domain.post.dto.request.PostUpdateRequest;
import com.cotato.kampus.domain.post.dto.response.MyPostResponse;
import com.cotato.kampus.domain.post.dto.response.PostCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostDeleteResponse;
import com.cotato.kampus.domain.post.dto.response.PostDetailResponse;
import com.cotato.kampus.domain.post.dto.response.PostDraftCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostDraftDetailResponse;
import com.cotato.kampus.domain.post.dto.response.PostDraftSliceFindResponse;
import com.cotato.kampus.domain.post.dto.response.PostSliceFindResponse;
import com.cotato.kampus.domain.post.dto.response.SearchKeywordDeleteResponse;
import com.cotato.kampus.domain.post.dto.response.SearchKeywordListResponse;
import com.cotato.kampus.domain.post.dto.response.SearchedPostResponse;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 Board)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
@RequestMapping("/v1/api/posts")
public class PostController {

	private final PostService postService;
	private final BoardService boardService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 생성", description = "게시글 생성 요청입니다. 사진이 없는 경우 빈 값('')을 보내지 말고, 해당 필드를 생략하거나 값을 보내지 않도록 해주세요.")
	public ResponseEntity<DataResponse<PostCreateResponse>> createPost(
		@Parameter(description = "Post creation request")
		@Valid @ModelAttribute PostCreateRequest request) throws ImageException {

		// 게시판이 카테고리를 사용하는지 확인
		boolean requiresCategory = boardService.requiresCategory(request.boardId());
		postService.validateCategoryForBoard(requiresCategory, request.postCategory());

		return ResponseEntity.ok(DataResponse.from(
				PostCreateResponse.of(
					postService.createPost(
						request.boardId(),
						request.title(),
						request.content(),
						request.postCategory(),
						request.images() == null ? List.of() : request.images()
					)
				)
			)
		);
	}

	@GetMapping("/boards/{boardId}")
	@Operation(summary = "게시판의 게시글 리스트 조회",
		description = "BoardId에 해당하는 게시글을 정렬 기준에 따라 조회합니다.(기본값: 최신순, 페이지당 게시글 수: 10)")
	public ResponseEntity<DataResponse<PostSliceFindResponse>> findPosts(
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "1") int page,
		@Parameter(
			name = "sort",
			description = "정렬 기준 (recent: 최신순, old: 오래된순, likes: 좋아요순)",
			schema = @Schema(type = "string", defaultValue = "recent",
				allowableValues = {"recent", "old", "likes"})
		)
		@RequestParam(required = false, defaultValue = "recent") PostSortType sort
	) {
		return ResponseEntity.ok(
			DataResponse.from(
				PostSliceFindResponse.from(
					postService.findPosts(boardId, page, sort)
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
		@Valid @ModelAttribute PostUpdateRequest request
	) throws ImageException {

		// 게시판이 카테고리를 사용하는지 확인
		Long boardId = postService.findPostDetail(postId).boardId();
		boolean requiresCategory = boardService.requiresCategory(boardId);
		postService.validateCategoryForBoard(requiresCategory, request.postCategory());

		postService.updatePost(
			postId,
			request.title(),
			request.content(),
			request.postCategory(),
			request.newImages() == null ? List.of() : request.newImages()); // 이미지 없는 경우 빈 리스트로 요청

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
	public ResponseEntity<DataResponse<PostDraftSliceFindResponse>> findDraftPostList(
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
				PostDraftSliceFindResponse.from(
					postService.findPostDrafts(boardId, page)
				)
			)
		);
	}

	@GetMapping(value = "/draft/{postDraftId}")
	@Operation(summary = "임시 저장 게시글 조회", description = "특정 임시 저장글을 조회합니다.")
	public ResponseEntity<DataResponse<PostDraftDetailResponse>> findDraftPost(
		@PathVariable Long postDraftId
	) {
		return ResponseEntity.ok(DataResponse.from(
				PostDraftDetailResponse.from(
					postService.findDraftDetail(postDraftId)
				)
			)
		);
	}

	@PostMapping(value = "/draft/{postDraftId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "임시 저장글 수정 발행")
	public ResponseEntity<DataResponse<PostCreateResponse>> publishDraftPost(
		@PathVariable Long postDraftId,
		@Valid @ModelAttribute PostUpdateRequest request
	) throws ImageException {

		// 게시판이 카테고리를 사용하는지 확인
		Long boardId = postService.findDraftDetail(postDraftId).boardId();
		boolean requiresCategory = boardService.requiresCategory(boardId);
		postService.validateCategoryForBoard(requiresCategory, request.postCategory());

		// 게시글 생성
		Long postId = postService.publishDraftPost(
			postDraftId,
			request.title(),
			request.content(),
			request.postCategory(),
			request.deletedImageUrls() == null ? List.of() : request.deletedImageUrls(),
			request.newImages() == null ? List.of() : request.newImages());

		// 임시 저장글 삭제
		postService.deleteDraftPosts(List.of(postDraftId));

		return ResponseEntity.ok(DataResponse.from(
				PostCreateResponse.of(postId)
			)
		);
	}

	@DeleteMapping(value = "/draft")
	@Operation(summary = "임시 저장 게시글 선택 삭제", description = "선택된 임시 저장글들을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteDraftPost(
		@RequestBody DraftDeleteRequest request
	) {
		postService.deleteDraftPosts(request.draftPostIds());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping(value = "boards/{boardId}/draft")
	@Operation(summary = "임시 저장 게시글 전체 삭제", description = "특정 게시판의 모든 임시 저장글을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteAllDraftPost(
		@PathVariable Long boardId
	) {
		postService.deleteAllDraftPost(boardId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my")
	@Operation(summary = "[마이페이지] 내가 쓴 게시글 조회", description = "현재 사용자가 작성한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<MyPostResponse>> findMyPosts(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
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
	) {
		postService.scrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{postId}/scrap")
	@Operation(summary = "게시글 스크랩 취소", description = "게시글 스크랩을 해제합니다.")
	public ResponseEntity<DataResponse<Void>> unscrapPost(
		@PathVariable Long postId
	) {
		postService.unscrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my/scrap")
	@Operation(summary = "[마이페이지] 스크랩한 게시글 조회", description = "현재 사용자가 스크랩한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<MyPostResponse>> findMyScrapedPosts(
		@RequestParam(required = false, defaultValue = "0") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
				MyPostResponse.from(
					postService.findUserScrapedPosts(page)
				)
			)
		);
	}

	@GetMapping("/search")
	@Operation(summary = "전체 게시글 검색")
	public ResponseEntity<DataResponse<SearchedPostResponse>> searchAllPosts(
		@RequestParam @NotBlank @Length(min = 2, max = 10, message = "keyword는 2자 이상 10자 이하로 구성해야 합니다.") String keyword,
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SearchedPostResponse.from(
				postService.searchAllPosts(keyword, page)
			)
		));
	}

	@GetMapping("/search/{boardId}")
	@Operation(summary = "게시판 내 게시글 검색")
	public ResponseEntity<DataResponse<SearchedPostResponse>> searchBoardPosts(
		@RequestParam @NotBlank @Length(min = 2, max = 10, message = "keyword는 2자 이상 10자 이하로 구성해야 합니다.") String keyword,
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SearchedPostResponse.from(
				postService.searchBoardPosts(keyword, boardId, page)
			)
		));
	}

	@GetMapping("/search/keywords")
	@Operation(summary = "게시글 검색 키워드 조회", description = "게시글 검색 키워드를 조회합니다.(최대 5개, 최신순 정렬)")
	public ResponseEntity<DataResponse<SearchKeywordListResponse>> searchAllPosts() {
		return ResponseEntity.ok(DataResponse.from(
			SearchKeywordListResponse.from(
				postService.findSearchKeyword()
			)
		));
	}

	@DeleteMapping("search/keywords/{keywordId}")
	@Operation(summary = "게시글 검색 키워드 단건 삭제", description = "게시글 검색 키워드 Id를 통해 삭제합니다.")
	public ResponseEntity<DataResponse<SearchKeywordDeleteResponse>> deleteKeyword(
		@PathVariable Long keywordId) {
		return ResponseEntity.ok(DataResponse.from(
			SearchKeywordDeleteResponse.from(
				postService.deleteSearchKeyword(keywordId)
			)
		));
	}
}