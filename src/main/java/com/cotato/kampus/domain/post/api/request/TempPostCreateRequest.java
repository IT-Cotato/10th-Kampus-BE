package com.cotato.kampus.domain.post.api.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TempPostCreateRequest(
	@NotNull(message = "boardId는 필수 값입니다.")
	Long boardId,

	@Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
	String title,

	@Size(max = 1000, message = "내용은 최대 1000자까지 입력할 수 있습니다.")
	String content,

	List<String> categories,
	List<MultipartFile> images
) {
	public TempPostCreateRequest   {
		if(images == null) {
			images = List.of();
		}

		if(categories == null) {
			categories = List.of();
		}
	}
}
