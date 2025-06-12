package com.cotato.kampus.domain.translation.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

@ExtendWith(MockitoExtension.class)
class PostTranslationTest {

	@Mock
	private Translator translator;

	@Test
	@DisplayName("번역 성공 - 제목과 내용이 모두 올바르게 번역된다")
	void translate_Success() throws DeepLException, InterruptedException {
		// Given
		String originalTitle = "Hello World";
		String originalContent = "This is a test post content.";
		String targetLanguage = "FR";
		String expectedTranslatedTitle = "Bonjour le monde";
		String expectedTranslatedContent = "Ceci est un contenu de message de test.";

		PostTranslation postTranslation = PostTranslation.builder()
			.title(originalTitle)
			.content(originalContent)
			.targetLanguageCode(targetLanguage)
			.build();

		when(translator.translateText(originalTitle, null, targetLanguage))
			.thenReturn(new TextResult(expectedTranslatedTitle, "EN", 11, null));
		when(translator.translateText(originalContent, null, targetLanguage))
			.thenReturn(new TextResult(expectedTranslatedContent, "EN", 32, null));

		// When
		PostTranslation result = postTranslation.translate(translator);

		// Then
		assertThat(result.getTranslatedTitle()).isEqualTo(expectedTranslatedTitle); // 번역된 제목
		assertThat(result.getTranslatedContent()).isEqualTo(expectedTranslatedContent); // 번역된 내용
	}

	@Test
	@DisplayName("번역 실패 - DeepLException 발생 시 AppException으로 변환")
	void translate_Fail_DeepLException() throws DeepLException, InterruptedException {
		// Given
		PostTranslation postTranslation = PostTranslation.builder()
			.title("Hello")
			.content("World")
			.targetLanguageCode("FR")
			.build();

		// When
		when(translator.translateText(anyString(), any(), anyString()))
			.thenThrow(new DeepLException("Translation API error"));
		// Then
		assertThatThrownBy(() -> postTranslation.translate(translator))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.DEEPL_TRANSLATION_ERROR.getMessage());

		assertThat(postTranslation.getTranslatedTitle()).isNull();
		assertThat(postTranslation.getTranslatedContent()).isNull();
	}

	@Test
	@DisplayName("번역 실패 - InterruptedException 발생 시 AppException으로 변환")
	void translate_Fail_InterruptedException() throws DeepLException, InterruptedException {
		// Given
		PostTranslation postTranslation = PostTranslation.builder()
			.title("Hello")
			.content("World")
			.targetLanguageCode("FR")
			.build();

		// When
		when(translator.translateText(anyString(), any(), anyString()))
			.thenThrow(new InterruptedException("Thread interrupted"));

		// When & Then
		assertThatThrownBy(() -> postTranslation.translate(translator))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.DEEPL_TRANSLATION_ERROR.getMessage());

		assertThat(postTranslation.getTranslatedTitle()).isNull();
		assertThat(postTranslation.getTranslatedContent()).isNull();
	}
}