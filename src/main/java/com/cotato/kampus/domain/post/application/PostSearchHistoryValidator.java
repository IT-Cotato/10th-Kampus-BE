package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dto.PostSearchHistoryWithUserId;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostSearchHistoryValidator {

	private final PostSearchHistoryFinder postSearchHistoryFinder;

	public void validateUser(Long userId, Long keywordId) {
		PostSearchHistoryWithUserId history = postSearchHistoryFinder.findById(keywordId);
		if (!history.userId().equals(userId)) {
			throw new AppException(ErrorCode.HISTORY_DELETE_FORBIDDEN);
		}
	}
}