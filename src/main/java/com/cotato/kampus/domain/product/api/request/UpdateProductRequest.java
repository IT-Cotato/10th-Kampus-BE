package com.cotato.kampus.domain.product.api.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
	@NotBlank(message = "상품 제목은 필수 항목입니다")
	String title,

	@NotNull(message = "상품 가격은 필수 항목입니다")
	@Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다")
	Integer price,

	@NotBlank(message = "상품 설명은 필수 항목입니다")
	String description,

	@NotNull(message = "카테고리는 필수입니다.")
	@NotEmpty(message = "카테고리는 최소 1개 이상 지정해야 합니다.")
	List<String> categoryNames,

	@NotNull(message = "상품 이미지 필드는 필수입니다")
	@NotEmpty(message = "상품 이미지는 최소 1개 이상 업로드해야 합니다")
	@Size(max = 10, message = "상품 이미지는 최대 10개까지 업로드 가능합니다")
	List<MultipartFile> images
) {
}
