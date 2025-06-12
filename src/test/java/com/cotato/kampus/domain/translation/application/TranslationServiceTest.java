package com.cotato.kampus.domain.translation.application;

import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.translation.dto.TranslatedPost;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

	@InjectMocks
	private TranslationService translationService;

	@Mock
	private PostTranslator postTranslator;
	@Mock
	private PostFinder postFinder;
	@Mock
	private ApiUserResolver apiUserResolver;
	@Mock
	private TranslationValidator translationValidator;
	@Mock
	private ProductFinder productFinder;
	@Mock
	private User mockUser;

	@Test
	@DisplayName("translateProduct - 성공")
	void translateProduct() {
		// Given
		Long productId = 1L;
		Product product = Product.create(1L, "title", 1000, "description");

		// When
		when(productFinder.findById(productId)).thenReturn(product);
		when(apiUserResolver.getCurrentUser()).thenReturn(mockUser);
		when(mockUser.getPreferredLanguage()).thenReturn(PreferredLanguage.KOREAN);
		when(postTranslator.translatePost(
			"title", "description", PreferredLanguage.KOREAN.getCode()))
			.thenReturn(new TranslatedPost("translatedTitle", "translatedDescription"));

		TranslatedPost translatedPost = translationService.translateProduct(productId);
		// Then
		Assertions.assertThat(translatedPost.title()).isEqualTo("translatedTitle");
		Assertions.assertThat(translatedPost.content()).isEqualTo("translatedDescription");
	}

	@Test
	@DisplayName("translateProduct 실패 - 중고거래 게시글 존재하지 않는 경우")
	void translateProduct_notFound() {
		// Given
		Long productId = 1L;

		// When
		when(productFinder.findById(productId)).thenThrow(new AppException(ErrorCode.PRODUCT_NOT_FOUND));

		// Then
		Assertions.assertThatThrownBy(() -> translationService.translateProduct(productId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
	}
}