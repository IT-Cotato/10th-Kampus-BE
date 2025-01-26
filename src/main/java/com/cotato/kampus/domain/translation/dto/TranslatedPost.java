package com.cotato.kampus.domain.translation.dto;

public record TranslatedPost(
	String title,
	String content
) {
	public static TranslatedPost of(String title, String content) {
		return new TranslatedPost(title, content);
	}
}
