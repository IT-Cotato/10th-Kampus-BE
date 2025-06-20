package com.cotato.kampus.domain.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductCategoryInfo;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryManager;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductCategoryService {

	private final ProductCategoryManager productCategoryManager;
	private final ProductCategoryFinder productCategoryFinder;
	private final UserValidator userValidator;

	@Transactional
	public Long createCategory(String categoryName) {
		// 1. 관리자 검증
		userValidator.validateAdminAccess();

		// 2. 카테고리 이름 중복 검증
		boolean isDuplicate = productCategoryFinder.existsByCategoryName(categoryName);
		if(isDuplicate) {
			throw new AppException(ErrorCode.PRODUCT_CATEGORY_DUPLICATED);
		}

		// 3. 카테고리 생성
		return productCategoryManager.append(categoryName).getId();
	}

	public List<ProductCategoryInfo> findAllCategories() {
		List<ProductCategory> categories = productCategoryFinder.findAll();

		return categories.stream().map(ProductCategoryInfo::from).toList();
	}

	@Transactional
	public void updateCategory(Long categoryId, String categoryName) {
		userValidator.validateAdminAccess();

		boolean isDuplicate = productCategoryFinder.existsByCategoryName(categoryName);
		if(isDuplicate) {
			throw new AppException(ErrorCode.PRODUCT_CATEGORY_DUPLICATED);
		}

		ProductCategory category = productCategoryFinder.find(categoryId);
		ProductCategory updatedCategory = category.withUpdateInfo(categoryName);
		productCategoryManager.update(updatedCategory);
	}
}
