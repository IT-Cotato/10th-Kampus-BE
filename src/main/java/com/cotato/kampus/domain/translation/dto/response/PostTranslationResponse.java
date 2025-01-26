package com.cotato.kampus.domain.translation.dto.response;

import com.cotato.kampus.domain.translation.dto.TranslatedPost;

public record PostTranslationResponse(
	String title,
	String content
) {
	public static PostTranslationResponse from(TranslatedPost translatedPost) {
		return new PostTranslationResponse(
			translatedPost.title(),
			translatedPost.content()
		);
	}
}