package com.cotato.kampus.domain.common.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageValidator {

	private static final Integer MAX_IMAGE_COUNT = 10;

	// 허용되는 이미지 MIME 타입
	private static final List<String> ALLOWED_MIME_TYPES = List.of(
		"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
		// "image/heic", "image/heif"  // iOS에서 주로 사용하는 형식
	);

	public List<MultipartFile> filterValidImages(List<MultipartFile> images) {
		return images.stream()
			.filter(image -> image != null && image.getOriginalFilename() != null && !image.getOriginalFilename().isEmpty())
			.toList();
	}

	/**
	 * 모든 이미지가 유효한지 검증하고, 유효하지 않은 이미지가 있을 경우 예외를 발생시킵니다.
	 *
	 *  @param images 검증할 이미지 파일 목록
	 *  @throws RuntimeException 유효하지 않은 이미지가 있을 경우 발생하는 예외
	 */
	public void validateImagesOrThrow(List<MultipartFile> images) {
		// 이미지 개수 검증
		if(images == null || images.isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_PHOTO_REQUIRED);
		}

		if(images.size() > MAX_IMAGE_COUNT) {
			throw new AppException(ErrorCode.IMAGE_SIZE_EXCEEDED);
		}

		List<String> invalidImageNames = new ArrayList<>();
		List<String> invalidTypeImageNames = new ArrayList<>();

		for(MultipartFile image : images) {
			if (image.isEmpty()) {
				invalidImageNames.add(image.getOriginalFilename() + " (빈 파일)");
				continue;
			}

			String contentType = image.getContentType();
			if(contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
				invalidTypeImageNames.add(image.getOriginalFilename() +
					(contentType != null ? " (타입: " + contentType + ")" : " (알 수 없는 타입)"));
				invalidImageNames.add(image.getOriginalFilename());
			}
		}

		// 유효하지 않은 이미지가 있으면 예외 발생
		if(!invalidImageNames.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder("다음 이미지 파일은 유효하지 않습니다: ");

			if(!invalidTypeImageNames.isEmpty()) {
				errorMessage.append("\n- 지원되지 않는 형식: ")
					.append(String.join(", ", invalidTypeImageNames))
					.append(" (지원 형식: ")
					.append(ALLOWED_MIME_TYPES.stream()
						.map(type -> type.substring(type.indexOf('/') + 1))
						.collect(Collectors.joining(", ")))
					.append(")");
			}

			throw new RuntimeException(errorMessage.toString());
		}
	}
}
