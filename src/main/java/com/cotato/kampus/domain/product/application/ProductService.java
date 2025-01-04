package com.cotato.kampus.domain.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.product.ProductCategory;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductService {

	private final ProductAppender productAppender;
	private final ProductPhotoAppender productPhotoAppender;
	private final S3Uploader s3Uploader;
	private static final String PRODUCT_IMAGE_FOLDER = "product";

	@Transactional
	public Long createProduct(String title, Long sellPrice, String description, ProductCategory productCategory,
		List<MultipartFile> imageFiles) throws ImageException {

		// s3에 이미지 업로드
		List<String> imageUrls = s3Uploader.uploadFiles(imageFiles, PRODUCT_IMAGE_FOLDER);

		// 상품 추가
		Long productId = productAppender.append(title, sellPrice, description, productCategory);

		// 상품 이미지 추가
		productPhotoAppender.appendAll(productId, imageUrls);

		return productId;
	}
}
