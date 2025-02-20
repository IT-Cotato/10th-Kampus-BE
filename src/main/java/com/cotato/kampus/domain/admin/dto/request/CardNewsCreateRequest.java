package com.cotato.kampus.domain.admin.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CardNewsCreateRequest(
	@NotBlank
	@Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
	String title,
	@Size(max = 1000, message = "내용은 최대 1000자까지 입력할 수 있습니다.")
	String content,
	@NotNull(message = "이미지는 필수입니다.")
	List<MultipartFile> images
) {
}
