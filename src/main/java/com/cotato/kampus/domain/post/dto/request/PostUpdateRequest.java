package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.post.enums.PostCategory;

import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
	@NotNull
	String title,
	@NotNull
	String content,
	PostCategory postCategory,
	List<MultipartFile> newImages,
	List<String> deletedImageUrls
) {
}