package com.cotato.kampus.domain.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.product.ProductManager;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductScrapService {

	private final ApiUserResolver apiUserResolver;
	private final UserValidator userValidator;
	private final ProductFinder productFinder;
	private final ProductManager productManager;
	private final ProductScrapFinder productScrapFinder;
	private final ProductScrapManager productScrapManager;

	@Transactional
	public void addScrap(Long productId) {
		// 1. 유저 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateStudentVerification(user);

		// 2. 상품 조회/검증
		Product product = productFinder.findById(productId);
		product.validateNotDeleted();

		// 3. 스크랩 여부 검증
		boolean isScrapped = productScrapFinder.isScrapped(productId, user.id());
		if(isScrapped) {
			throw new AppException(ErrorCode.ALREADY_SCRAPPED_PRODUCT);
		}

		// 4. 스크랩 추가
		productScrapManager.append(productId, user.id());

		// 5. 상품 스크랩 수 반영
		Product scrappedProduct = product.increaseScrapCount();
		productManager.update(scrappedProduct);
	}

	@Transactional
	public void removeScrap(Long productId) {
		// 1. 유저, 상품 조회
		UserDto user = apiUserResolver.getCurrentUserDto();
		Product product = productFinder.findById(productId);

		// 2. 스크랩 여부 검증
		boolean isScrapped = productScrapFinder.isScrapped(productId, user.id());
		if(!isScrapped) {
			throw new AppException(ErrorCode.PRODUCT_SCRAP_NOT_FOUND);
		}

		// 3. 스크랩 취소
		productScrapManager.delete(product.getId(), user.id());

		// 4. 상품 스크랩 수 감소
		Product unscrappedProduct = product.decreaseScrapCount();
		productManager.update(unscrappedProduct);
	}

}
