package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostCreateRequest(
	Long boardId,
	String title,
	String content,
	PostCategory postCategory,
	List<MultipartFile> images
) {
}
