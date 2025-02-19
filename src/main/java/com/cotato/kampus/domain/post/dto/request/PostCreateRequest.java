package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.post.enums.PostCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
	@NotNull
	Long boardId,

	@NotBlank
	@Size(max=30)
	String title,

	@NotBlank
	@Size(max=1000)
	String content,

	PostCategory postCategory,
	List<MultipartFile> images
) {
}
