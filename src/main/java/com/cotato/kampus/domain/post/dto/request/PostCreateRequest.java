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
	@Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
	String title,

	@NotBlank
	@Size(max = 1000, message = "내용은 최대 1000자까지 입력할 수 있습니다.")
	String content,

	PostCategory postCategory,
	List<MultipartFile> images
) {
}
