package com.cotato.kampus.domain.post.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.request.DraftDeleteRequest;
import com.cotato.kampus.domain.post.api.request.TempPostCreateRequest;
import com.cotato.kampus.domain.post.api.request.TempPostPublishRequest;
import com.cotato.kampus.domain.post.api.response.PostCreateResponse;
import com.cotato.kampus.domain.post.api.response.TempPostCreateResponse;
import com.cotato.kampus.domain.post.api.response.TempPostDetailResponse;
import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.post.api.response.TempPostSliceResponse;
import com.cotato.kampus.domain.post.application.TemporaryPostService;
import com.cotato.kampus.domain.post.domain.TempPostThumbnail;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 BoardEntity)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
@RequestMapping("/v1/api/posts")
public class TemporaryPostController {

	private final TemporaryPostService temporaryPostService;

	@PostMapping(value = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시글 임시 저장", description = "게시글을 임시 저장합니다. boardId는 필수 값입니다. 사진이 없는 경우 빈 값('')을 보내지 말고, 해당 필드를 생략하거나 값을 보내지 않도록 해주세요.")
	public ResponseEntity<DataResponse<TempPostCreateResponse>> draftPost(
		@Parameter(description = "Post creation request")
		@Valid @ModelAttribute TempPostCreateRequest request) throws ImageException {
		return ResponseEntity.ok(DataResponse.from(
			TempPostCreateResponse.of(
				temporaryPostService.createTempPost(
					request.boardId(),
					request.title(),
					request.content(),
					request.categories(),
					request.images()
				)
			)
		));
	}

	@GetMapping(value = "/draft")
	@Operation(summary = "임시 저장글 목록 조회", description = "모든 임시 저장글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<TempPostSliceResponse<TempPostThumbnail>>> findDraftList(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		int totalCount = temporaryPostService.findTempPostCount();
		return ResponseEntity.ok(DataResponse.from(
				TempPostSliceResponse.from(
					temporaryPostService.findPostDrafts(page)
					,totalCount
				)
			)
		);
	}

	@GetMapping(value = "/draft/{postDraftId}")
	@Operation(summary = "임시 저장 게시글 조회", description = "특정 임시 저장글을 조회합니다.")
	public ResponseEntity<DataResponse<TempPostDetailResponse>> findDraftPost(
		@PathVariable Long postDraftId
	) {
		return ResponseEntity.ok(DataResponse.from(
				TempPostDetailResponse.from(
					temporaryPostService.findTempPostDetail(postDraftId)
				)
			)
		);
	}

	@PostMapping(value = "/draft/{postDraftId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "임시 저장글 발행", description = "새 데이터로 게시글을 생성하고, 기존 임시 게시글은 삭제됩니다.")
	public ResponseEntity<DataResponse<PostCreateResponse>> publishTempPost(
		@PathVariable Long postDraftId,
		@Valid @ModelAttribute TempPostPublishRequest request) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
				PostCreateResponse.of(
					temporaryPostService.publishDraftPost(
						postDraftId,
						request.title(),
						request.content(),
						request.categories(),
						request.images()
					)
				)
			)
		);
	}

	@PatchMapping(value = "/draft/{postDraftId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "임시 저장글 수정", description = "기존 임시 저장글에 덮어씁니다.")
	public ResponseEntity<DataResponse<TempPostCreateResponse>> updateDraftPost(
		@PathVariable Long postDraftId,
		@Valid @ModelAttribute TempPostCreateRequest request) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
			TempPostCreateResponse.of(
				temporaryPostService.updateDraftPost(
					postDraftId,
					request.title(),
					request.content(),
					request.categories(),
					request.images()
				)
			)
		));
	}

	@DeleteMapping(value = "/draft/select")
	@Operation(summary = "임시 저장 게시글 선택 삭제", description = "선택된 임시 저장글들을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteDraftPost(
		@RequestBody DraftDeleteRequest request
	) {
		temporaryPostService.deleteSelectedTempPosts(request.tempPostIds());
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping(value = "/draft/all")
	@Operation(summary = "임시 저장 게시글 전체 삭제", description = "모든 임시 저장글을 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteAllDraftPost() {
		temporaryPostService.deleteAllTempPost();
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping(value = "/draft/count")
	@Operation(summary = "임시 게시글 개수 조회", description = "임시 게시글 개수를 조회합니다.")
	public ResponseEntity<DataResponse<Integer>> findTempPostCount() {
			return ResponseEntity.ok(DataResponse.from(
				temporaryPostService.findTempPostCount()
			)
		);
	}
}
