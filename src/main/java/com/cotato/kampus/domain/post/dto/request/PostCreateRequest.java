package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.enums.Anonymity;

public record PostCreateRequest(
	Long boardId,
	String title,
	String content,
	String postCategory,
	Anonymity anonymity,
	List<MultipartFile> images
) {
}
