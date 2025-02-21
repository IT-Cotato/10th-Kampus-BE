package com.cotato.kampus.domain.support.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateInquiryRequest(
	@NotBlank
	@Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
	String title,

	@NotBlank
	@Size(max = 1000, message = "내용은 최대 1000자까지 입력할 수 있습니다.")
	String content,

	List<MultipartFile> images
) {
}
