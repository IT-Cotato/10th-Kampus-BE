package com.cotato.kampus.domain.support.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.support.dto.InquiryPreview;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SupportService {

	private final ApiUserResolver apiUserResolver;
	private final InquiryAppender inquiryAppender;
	private final ImageValidator imageValidator;
	private final S3Uploader s3Uploader;
	private final InquiryPhotoAppender inquiryPhotoAppender;
	private final InquiryFinder inquiryFinder;

	private static final String INQUIRY_IMAGE_FOLDER = "inquiry";

	@Transactional
	public void createInquiry(
		String title,
		String content,
		List<MultipartFile> images
	) throws ImageException {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// Inquiry 추가
		Long inquiryId = inquiryAppender.append(userId, title, content);

		// 유효한 이미지만 필터링
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, INQUIRY_IMAGE_FOLDER);

		// InquiryPhoto 추가
		if (!imageUrls.isEmpty()) {
			inquiryPhotoAppender.appendAll(inquiryId, imageUrls);
		}
	}

	@Transactional
	public Slice<InquiryPreview> findUserInquiries(int page) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		return inquiryFinder.findAllUserInquiry(userId, page);
	}
}
