package com.cotato.kampus.domain.translation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TranslationValidator {

	public void validatePost(String title, String content) throws AppException {
		if (title == null || title.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_DEEPL_CONTENT);
		}
		if (content == null || content.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_DEEPL_CONTENT);
		}
	}
}