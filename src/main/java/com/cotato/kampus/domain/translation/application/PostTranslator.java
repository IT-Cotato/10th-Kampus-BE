package com.cotato.kampus.domain.translation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.translation.domain.PostTranslation;
import com.cotato.kampus.domain.translation.dto.TranslatedPost;
import com.cotato.kampus.domain.translation.dto.TranslatedText;
import com.deepl.api.DeepLException;
import com.deepl.api.Translator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/*
 게시글에서 사용되는 번역을 담당
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTranslator {
	private final Translator translator;

	public TranslatedPost translatePost(String title, String content, String languageCode) throws
		DeepLException, InterruptedException {
		PostTranslation postTranslation = PostTranslation.builder()
			.title(title)
			.content(content)
			.targetLanguageCode(languageCode)
			.build();
		PostTranslation translated = postTranslation.translate(translator);
		return TranslatedPost.of(translated.getTranslatedTitle(), translated.getTranslatedContent());
	}

	public TranslatedText translateText(String content, String languageCode) throws
		DeepLException, InterruptedException {
		return TranslatedText.from(
			translator.translateText(content, null, languageCode).getText()
		);
	}
}