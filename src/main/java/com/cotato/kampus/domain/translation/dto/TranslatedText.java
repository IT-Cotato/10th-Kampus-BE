package com.cotato.kampus.domain.translation.dto;

public record TranslatedText(
	String content
) {
	public static TranslatedText from(String content) {
		return new TranslatedText(content);
	}
}