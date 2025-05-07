package com.cotato.kampus.domain.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.product.ProductSaver;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryMappingAdapter;
import com.cotato.kampus.domain.product.implement.productPhoto.ProductPhotoAppender;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductService {

	private final ProductSaver productSaver;
	private final ProductPhotoAppender productPhotoAppender;
	private final S3Uploader s3Uploader;
	private static final String PRODUCT_IMAGE_FOLDER = "product";
	private final ApiUserResolver apiUserResolver;
	private final UserValidator userValidator;
	private final ImageValidator imageValidator;
	private final ProductCategoryFinder productCategoryFinder;
	private final ProductCategoryMappingAdapter productCategoryMappingAdapter;
	private final ProductFinder productFinder;
	private final ProductScrapFinder productScrapFinder;
	private final ProductScrapManager productScrapManager;

	@Transactional
	public Long createProduct(
		String title,
		Integer price,
		String description,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateStudentVerification(user);

		// 2. 카테고리 검증/조회
		if (categoryNames == null || categoryNames.isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_CATEGORY_REQUIRED);
		}
		List<Long> categoryIds = categoryNames.stream()
			.map(productCategoryFinder::find)
			.map(ProductCategory::getId)
			.toList();

		// 3. 유효한 이미지 필터링 & S3 업로드
		imageValidator.validateProductImages(images);
		List<String> imageUrls = s3Uploader.uploadFiles(images, PRODUCT_IMAGE_FOLDER);

		// 3. Product 추가
		Product product = productSaver.append(user.id(), title, price, description);
		productCategoryMappingAdapter.saveAll(product.getId(), categoryIds);
		productPhotoAppender.appendAll(product.getId(), imageUrls);

		return product.getId();
	}

	@Transactional
	public void deleteProduct(Long productId) {
		// 1. 유저, 상품 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		Product product = productFinder.findById(productId);
		product.validateDeletable(user.id());

		// 2. 상품 상태를 삭제로 업데이트
		Product updatedProduct = product.withProductStatus(ProductStatus.DELETED);
		productSaver.update(updatedProduct);
	}

	@Transactional
	public void addScrap(Long productId) {
		// 1. 유저 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateStudentVerification(user);

		// 2. 상품 조회/검증
		Product product = productFinder.findById(productId);
		product.validateNotDeleted();

		// 3. 스크랩 여부 검증
		boolean isAlreadyScrapped = productScrapFinder.isAlreadyScrapped(productId, user.id());
		if(isAlreadyScrapped) {
			throw new AppException(ErrorCode.ALREADY_SCRAPPED_PRODUCT);
		}

		// 4. 스크랩 추가
		productScrapManager.append(productId, user.id());

		// 5. 상품 스크랩 수 반영
		Product scrappedPost = product.increaseScrapCount();
		productSaver.update(scrappedPost);
	}
}
