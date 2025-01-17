package com.cotato.kampus.domain.post.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.post.application.PostService;
import com.cotato.kampus.domain.post.dto.request.PostCreateRequest;
import com.cotato.kampus.domain.post.dto.response.PostCreateResponse;
import com.cotato.kampus.domain.post.dto.response.PostDeleteResponse;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.cotato.kampus.global.error.exception.ImageException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/posts")
public class PostController {

	private final PostService postService;

	@PostMapping("")
	public ResponseEntity<DataResponse<PostCreateResponse>> createPost(
		@RequestPart PostCreateRequest request,
		@RequestPart(required = false) List<MultipartFile> images) throws ImageException {

		return ResponseEntity.ok(DataResponse.from(
			PostCreateResponse.of(
				postService.createPost(
				request.boardId(),
				request.title(),
				request.content(),
				request.postCategory(),
				request.anonymity(),
				images
				)
			)
		));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DataResponse<PostDeleteResponse>> deletePost(
		@PathVariable Long postId
	){

		return ResponseEntity.ok(DataResponse.from(
			PostDeleteResponse.of(
				postService.deletePost(
					postId
				)
			)
		));
	}
}
