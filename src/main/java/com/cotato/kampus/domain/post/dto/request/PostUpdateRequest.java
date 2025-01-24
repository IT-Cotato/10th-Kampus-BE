package com.cotato.kampus.domain.post.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostCategory;

import jakarta.annotation.Nullable;

public record PostUpdateRequest(
	String title,
	String content,
	PostCategory postCategory,
	Anonymity anonymity,
	@Nullable
	List<MultipartFile> images
) {
}