package com.cotato.kampus.domain.translation.domain;

import com.deepl.api.DeepLException;
import com.deepl.api.Translator;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostTranslation {

	private String title;
	private String content;
	private String targetLanguageCode;
	private String translatedTitle;
	private String translatedContent;

	@Builder
	public PostTranslation(String title, String content, String targetLanguageCode) {
		this.title = title;
		this.content = content;
		this.targetLanguageCode = targetLanguageCode;
	}

	public PostTranslation translate(Translator translator) throws DeepLException, InterruptedException {
		translatedTitle = translator.translateText(title, null, targetLanguageCode).getText();
		translatedContent = translator.translateText(content, null, targetLanguageCode).getText();
		return this;
	}
}