package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostSearchHistoryRepository;
import com.cotato.kampus.domain.post.domain.PostSearchHistory;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryDto;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryList;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryWithUserId;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostSearchHistoryFinder {

	private final PostSearchHistoryRepository postSearchHistoryRepository;

	public PostSearchHistoryList findByUserId(Long userId) {
		return PostSearchHistoryList.from(
			postSearchHistoryRepository.findByUserIdOrderByCreatedTimeDesc(userId)
				.stream()
				.map(PostSearchHistoryDto::from)
				.toList()
		);
	}

	public PostSearchHistoryWithUserId findById(Long keywordId) {
		PostSearchHistory postSearchHistory = postSearchHistoryRepository.findById(keywordId)
			.orElseThrow(() -> new AppException(ErrorCode.HISTORY_NOT_FOUND));
		return PostSearchHistoryWithUserId.from(postSearchHistory);
	}
}