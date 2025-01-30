package com.cotato.kampus.domain.common.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageValidator {

	public List<MultipartFile> filterValidImages(List<MultipartFile> images) {
		return images.stream()
			.filter(image -> image != null && image.getOriginalFilename() != null && !image.getOriginalFilename().isEmpty())
			.toList();
	}

	public void validateDeletableImages(List<String> originalImageUrls, List<String> imageUrlsToDelete){
		List<String> invalidDeletes = imageUrlsToDelete.stream()
			.filter(image -> !originalImageUrls.contains(image))
			.toList();

		if (!invalidDeletes.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_DELETED_IMAGE);
		}
	}
}
