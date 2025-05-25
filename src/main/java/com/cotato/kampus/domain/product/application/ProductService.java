package com.cotato.kampus.domain.product.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.product.enums.ProductSortType;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductDetails;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.implement.product.ProductDtoMapper;
import com.cotato.kampus.domain.product.implement.product.ProductManager;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryMappingFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryMappingManager;
import com.cotato.kampus.domain.product.implement.productPhoto.ProductPhotoManager;
import com.cotato.kampus.domain.product.implement.productPhoto.ProductPhotoFinder;
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

	private final ProductManager productManager;
	private final ProductPhotoManager productPhotoManager;
	private final S3Uploader s3Uploader;
	private static final String PRODUCT_IMAGE_FOLDER = "product";
	private final ApiUserResolver apiUserResolver;
	private final UserValidator userValidator;
	private final ImageValidator imageValidator;
	private final ProductCategoryFinder productCategoryFinder;
	private final ProductCategoryMappingManager productCategoryMappingManager;
	private final ProductFinder productFinder;
	private final ProductScrapFinder productScrapFinder;
	private final ProductPhotoFinder productPhotoFinder;
	private final ProductCategoryMappingFinder productCategoryMappingFinder;
	private final ProductDtoMapper productDtoMapper;

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
		Product product = productManager.append(user.id(), title, price, description);
		productCategoryMappingManager.saveAll(product.getId(), categoryIds);
		productPhotoManager.appendAll(product.getId(), imageUrls);

		return product.getId();
	}

	@Transactional
	public void deleteProduct(Long productId) {
		// 1. 유저, 상품 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		Product product = productFinder.findById(productId);
		product.validateEditable(user.id());

		// 2. 상품 상태를 삭제로 업데이트
		Product updatedProduct = product.withProductStatus(ProductStatus.DELETED);
		productManager.update(updatedProduct);
	}

	@Transactional
	public void updateProduct(
		Long productId,
		String title,
		Integer price,
		String description,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저, 상품 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Product product = productFinder.findById(productId);
		product.validateEditable(userId);

		// 2. 카테고리 조회/검증
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

		// 4. 기존 이미지 S3 제거
		List<String> deleteImageUrls = productPhotoFinder.findAll(productId).stream()
			.map(ProductPhoto::getPhotoUrl)
			.toList();
		s3Uploader.deleteFiles(deleteImageUrls);

		// 5. 기존 데이터 제거
		productPhotoManager.deleteAll(productId);
		productCategoryMappingManager.deleteAll(productId);

		// 6. 상품 정보 업데이트
		product = product.withUpdateInfo(title, price, description);
		productManager.update(product);
		productCategoryMappingManager.saveAll(productId, categoryIds);
		productPhotoManager.appendAll(product.getId(), imageUrls);
	}

	@Transactional
	public ProductDetails findProductDetails(Long productId) {
		// 1. 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		// 2. 상품 조회/검증
		Product product = productFinder.findById(productId);
		product.validateNotDeleted();

		// 2. 관련 데이터 조회
		List<ProductPhoto> photos = productPhotoFinder.findAll(productId);
		boolean isAuthor = product.getUserId().equals(user.id());
		boolean isScrapped = productScrapFinder.isScrapped(productId, user.id());

		// 3. 상품 조회수 증가
		Product viewedProduct = product.increaseViewCount();
		productManager.update(viewedProduct);

		// 4. ProductDetails 변환
		return ProductDetails.of(viewedProduct, user.nickname(), photos, isAuthor, isScrapped);
	}

	public Slice<ProductThumbnail> findProducts(int page, int size, ProductSortType sort, String categoryName) {
		// 1. 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		Slice<Product> products;

		// 2. 카테고리 여부에 따른 필터링
		if(categoryName != null && !categoryName.isEmpty()) {
			ProductCategory category = productCategoryFinder.find(categoryName);
			List<Long> productIds = productCategoryMappingFinder.getIdsByCategory(category.getId());
			products = productFinder.findAllByProductIds(productIds, page, size, sort);
		} else {
			products = productFinder.findAll(page, size, sort);
		}

		return productDtoMapper.toProductThumbnails(products, user.id());
	}

	@Transactional
	public void updateStatus(Long productId, ProductStatus status) {
		// 1. 유저, 상품 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Product product = productFinder.findById(productId);
		product.validateEditable(userId);

		Product updatedProduct = product.withProductStatus(status);
		productManager.update(updatedProduct);
	}
}
