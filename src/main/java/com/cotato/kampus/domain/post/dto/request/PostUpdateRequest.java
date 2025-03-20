package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
	@NotNull
	String title,
	@NotNull
	String content,
	List<String> categories,
	List<MultipartFile> newImages,
	List<String> deletedImageUrls
) {
}