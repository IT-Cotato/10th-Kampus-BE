package com.cotato.kampus.domain.post.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.post.application.PostInteractionService;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 BoardEntity)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
@RequestMapping("/v1/api/posts")
public class PostInteractionController {

	private final PostInteractionService postInteractionService;

	@GetMapping("/my")
	@Operation(summary = "[마이페이지] 내가 쓴 게시글 조회", description = "현재 사용자가 작성한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnailWithBoardName>>> findMyPosts(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SliceResponse.from(
					postInteractionService.findUserPosts(page)
				)
			)
		);
	}

	@PostMapping("/{postId}/likes")
	@Operation(summary = "게시글 좋아요", description = "게시글 좋아요")
	public ResponseEntity<DataResponse<Void>> likePost(
		@PathVariable Long postId
	) {
		postInteractionService.likePost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{postId}/likes")
	@Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요 취소")
	public ResponseEntity<DataResponse<Void>> unlikePost(
		@PathVariable Long postId
	) {
		postInteractionService.unlikePost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@PostMapping("/{postId}/scrap")
	@Operation(summary = "게시글 스크랩", description = "게시글을 스크랩합니다.")
	public ResponseEntity<DataResponse<Void>> scrapPost(
		@PathVariable Long postId
	) {
		postInteractionService.scrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@DeleteMapping("/{postId}/scrap")
	@Operation(summary = "게시글 스크랩 취소", description = "게시글 스크랩을 해제합니다.")
	public ResponseEntity<DataResponse<Void>> unscrapPost(
		@PathVariable Long postId
	) {
		postInteractionService.unscrapPost(postId);
		return ResponseEntity.ok(DataResponse.ok());
	}

	@GetMapping("/my/scrap")
	@Operation(summary = "[마이페이지] 스크랩한 게시글 조회", description = "현재 사용자가 스크랩한 게시글을 최신순으로 조회합니다.")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnailWithBoardName>>> findMyScrapedPosts(
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SliceResponse.from(
					postInteractionService.findUserScrapedPosts(page)
				)
			)
		);
	}
}
