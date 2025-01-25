package com.cotato.kampus.domain.common.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageValidator {

	public List<MultipartFile> filterValidImages(List<MultipartFile> images) {
		if (images == null) {
			return List.of(); // null인 경우 빈 리스트 반환
		}

		return images.stream()
			.filter(image -> image != null && image.getOriginalFilename() != null && !image.getOriginalFilename().isEmpty())
			.toList();
	}
}
