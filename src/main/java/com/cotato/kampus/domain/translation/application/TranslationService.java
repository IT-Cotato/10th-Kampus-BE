package com.cotato.kampus.domain.translation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.translation.dto.TranslatedPost;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.deepl.api.DeepLException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TranslationService {

	private final PostTranslator postTranslator;
	private final PostFinder postFinder;
	private final ApiUserResolver apiUserResolver;

	public TranslatedPost translatePost(Long postId) throws DeepLException, InterruptedException {
		// 1. 게시글 조회
		PostDto post = postFinder.findPost(postId);

		// 2. 사용자의 선호 언어 조회
		PreferredLanguage preferredLanguage = apiUserResolver.getUser().getPreferredLanguage();

		// 3. 번역
		return postTranslator.translatePost(post.title(), post.content(), preferredLanguage.getCode());
	}

	public TranslatedPost translatePost(String title, String content, String targetLanguageCode) throws
		DeepLException, InterruptedException {
		return postTranslator.translatePost(title, content, targetLanguageCode);
	}
}