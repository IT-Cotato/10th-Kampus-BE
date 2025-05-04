package com.cotato.kampus.domain.common.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageValidationException;

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
	 * 상품 등록용 이미지 검증 (상품 사진 필수)
	 * 이미지가 없거나 비어있으면 예외 발생
	 *
	 *  @param images 검증할 이미지 파일 목록
	 *  @throws AppException PRODUCT_PHOTO_REQUIRED - 상품 사진이 없을 때 발생
	 *  @throws AppException IMAGE_SIZE_EXCEEDED - 최대 10개 초과 시 발생
	 *  @throws RuntimeException - 유효하지 않은 이미지 형식일 때 발생
	 */
	public void validateProductImages(List<MultipartFile> images) {
		// 이미지 필수 여부 검증
		if(images == null || images.isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_PHOTO_REQUIRED);
		}
		// 공통 이미지 검증 로직 사용
		validateImagesOrThrow(images);
	}

	/**
	 * 공통 이미지 검증 로직
	 * - 이미지 개수 검증
	 * - 이미지 MIME 타입 검증
	 * - 빈 파일 검증
	 *
	 *  @param images 검증할 이미지 파일 목록
	 *  @throws AppException IMAGE_SIZE_EXCEEDED - 최대 10개 초과 시 발생
	 *  @throws RuntimeException - 유효하지 않은 이미지 형식일 때 발생
	 */
	public void validateImagesOrThrow(List<MultipartFile> images) {
		// null 체크는 호출하는 메서드에서 처리
		if (images == null) {
			return;
		}

		// 이미지 개수 검증
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
			StringBuilder errorMessage = new StringBuilder();

			if(!invalidTypeImageNames.isEmpty()) {
				errorMessage.append("지원되지 않는 형식: ")
					.append(String.join(", ", invalidTypeImageNames))
					.append(" (지원 형식: ")
					.append(ALLOWED_MIME_TYPES.stream()
						.map(type -> type.substring(type.indexOf('/') + 1))
						.collect(Collectors.joining(", ")))
					.append(")");
			}

			throw new ImageValidationException(ErrorCode.INVALID_IMAGE_FORMAT, errorMessage.toString());
		}
	}
}
