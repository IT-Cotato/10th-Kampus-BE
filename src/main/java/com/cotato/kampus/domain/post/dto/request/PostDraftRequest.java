package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public record PostDraftRequest(
	@NotNull(message = "boardId는 필수 값입니다.")
	Long boardId,
	String title,
	String content,
	List<String> categories,
	List<MultipartFile> images
) {
}
