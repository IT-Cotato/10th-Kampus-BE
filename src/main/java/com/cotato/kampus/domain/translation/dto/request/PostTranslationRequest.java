package com.cotato.kampus.domain.translation.dto.request;

public record PostTranslationRequest(
	String title,
	String content,
	String targetLanguageCode
) {
}