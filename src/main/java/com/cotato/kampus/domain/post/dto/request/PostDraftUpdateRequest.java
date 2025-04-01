package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record PostDraftUpdateRequest (
	String title,
	String content,
	List<String> categories,
	List<MultipartFile> images
) {

}
