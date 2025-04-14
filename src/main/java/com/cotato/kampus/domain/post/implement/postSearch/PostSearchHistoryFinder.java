package com.cotato.kampus.domain.post.implement.postSearch;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.repository.PostSearchHistoryRepository;
import com.cotato.kampus.domain.post.domain.PostSearchHistory;
import com.cotato.kampus.domain.post.domain.PostSearchHistoryDto;
import com.cotato.kampus.domain.post.domain.PostSearchHistoryList;
import com.cotato.kampus.domain.post.domain.PostSearchHistoryWithUserId;
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