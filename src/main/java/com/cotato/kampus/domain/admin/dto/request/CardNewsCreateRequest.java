package com.cotato.kampus.domain.admin.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CardNewsCreateRequest(
	@NotBlank
	String title,
	@NotEmpty(message = "이미지는 필수입니다.")
	List<MultipartFile> images
) {
}
