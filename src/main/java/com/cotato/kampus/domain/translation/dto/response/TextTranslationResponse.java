package com.cotato.kampus.domain.translation.dto.response;

import com.cotato.kampus.domain.translation.dto.TranslatedText;

public record TextTranslationResponse(
	String content
) {
	public static TextTranslationResponse from(TranslatedText translatedText) {
		return new TextTranslationResponse(translatedText.content());
	}
}