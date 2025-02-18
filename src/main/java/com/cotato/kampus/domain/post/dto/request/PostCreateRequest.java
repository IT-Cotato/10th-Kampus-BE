package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.post.enums.PostCategory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest(
	@NotNull
	Long boardId,

	@NotBlank
	@Max(30)
	String title,

	@NotBlank
	@Max(1000)
	String content,

	PostCategory postCategory,
	List<MultipartFile> images
) {
}
