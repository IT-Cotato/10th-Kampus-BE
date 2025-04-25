package com.cotato.kampus.domain.post.api;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.post.api.response.SearchKeywordDeleteResponse;
import com.cotato.kampus.domain.post.api.response.SearchKeywordListResponse;
import com.cotato.kampus.domain.post.api.response.SliceResponse;
import com.cotato.kampus.domain.post.application.PostSearchService;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글(Post) API", description = "게시글 관련 API(게시판 API는 BoardEntity)")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
@RequestMapping("/v1/api/posts")
public class PostSearchController {
	private final PostSearchService postSearchService;

	@GetMapping("/search")
	@Operation(summary = "전체 게시글 검색")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnailWithBoardName>>> searchAllPosts(
		@RequestParam @NotBlank @Length(min = 2, max = 10, message = "keyword는 2자 이상 10자 이하로 구성해야 합니다.") String keyword,
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SliceResponse.from(
				postSearchService.searchAllPosts(keyword, page)
			)
		));
	}

	@GetMapping("/search/{boardId}")
	@Operation(summary = "게시판 내 게시글 검색")
	public ResponseEntity<DataResponse<SliceResponse<PostThumbnail>>> searchBoardPosts(
		@RequestParam @NotBlank @Length(min = 2, max = 10, message = "keyword는 2자 이상 10자 이하로 구성해야 합니다.") String keyword,
		@PathVariable Long boardId,
		@RequestParam(required = false, defaultValue = "1") int page
	) {
		return ResponseEntity.ok(DataResponse.from(
			SliceResponse.from(
				postSearchService.searchBoardPosts(keyword, boardId, page)
			)
		));
	}

	@GetMapping("/search/keywords")
	@Operation(summary = "게시글 검색 키워드 조회", description = "게시글 검색 키워드를 조회합니다.(최대 10개, 최신순 정렬)")
	public ResponseEntity<DataResponse<SearchKeywordListResponse>> searchAllPosts() {
		return ResponseEntity.ok(DataResponse.from(
			SearchKeywordListResponse.from(
				postSearchService.findSearchKeyword()
			)
		));
	}

	@DeleteMapping("search/keywords/{keywordId}")
	@Operation(summary = "게시글 검색 키워드 단건 삭제", description = "게시글 검색 키워드 Id를 통해 삭제합니다.")
	public ResponseEntity<DataResponse<SearchKeywordDeleteResponse>> deleteKeyword(
		@PathVariable Long keywordId) {
		return ResponseEntity.ok(DataResponse.from(
			SearchKeywordDeleteResponse.from(
				postSearchService.deleteSearchKeyword(keywordId)
			)
		));
	}

	@DeleteMapping("search/keywords")
	@Operation(summary = "게시글 검색 키워드 단건 삭제", description = "게시글 검색 키워드 Id를 통해 삭제합니다.")
	public ResponseEntity<DataResponse<Void>> deleteAllKeyword() {
		postSearchService.deleteAllSearchKeyword();
		return ResponseEntity.ok(DataResponse.ok());
	}

}
