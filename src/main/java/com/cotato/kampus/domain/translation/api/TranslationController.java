package com.cotato.kampus.domain.translation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.translation.application.TranslationService;
import com.cotato.kampus.domain.translation.dto.request.PostTranslationRequest;
import com.cotato.kampus.domain.translation.dto.response.PostTranslationResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.deepl.api.DeepLException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "번역 API", description = "번역 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/translations")
public class TranslationController {

	private final TranslationService translationService;

	@PostMapping("{postId}")
	@Operation(summary = "게시글 번역", description = "게시글 id를 통해 게시글을 번역합니다.")
	public ResponseEntity<DataResponse<PostTranslationResponse>> translatePost(@PathVariable Long postId) throws
		DeepLException,
		InterruptedException {
		return ResponseEntity.ok(DataResponse.from(
				PostTranslationResponse.from(
					translationService.translatePost(postId)
				)
			)
		);
	}

	@PostMapping("")
	@Operation(summary = "작성 게시글 번역", description = "작성중인 게시글을 번역합니다.")
	public ResponseEntity<DataResponse<PostTranslationResponse>> translateWritingPost(@RequestBody PostTranslationRequest request) throws
		DeepLException,
		InterruptedException {
		return ResponseEntity.ok(DataResponse.from(
				PostTranslationResponse.from(
					translationService.translatePost(request.title(), request.content(), request.targetLanguageCode())
				)
			)
		);
	}
}