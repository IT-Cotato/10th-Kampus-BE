package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostSearchHistoryRepository;
import com.cotato.kampus.domain.post.domain.PostSearchHistory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostSearchHistoryAppender {

	private final PostSearchHistoryRepository searchHistoryRepository;
	private static final int MAX_HISTORY_SIZE = 10;

	@Transactional
	public void append(Long userId, String keyword) {
		removeDuplicateKeyword(userId, keyword);
		removeOldKeyword(userId);
		saveNewHistory(userId, keyword);
	}

	private void removeDuplicateKeyword(Long userId, String keyword) {
		searchHistoryRepository.findByUserIdAndKeyword(userId, keyword)
			.ifPresent(searchHistoryRepository::delete);
	}

	private void removeOldKeyword(Long userId) {
		List<PostSearchHistory> histories = searchHistoryRepository.findTop5ByUserIdOrderByCreatedTimeDesc(userId);
		if (histories.size() >= MAX_HISTORY_SIZE) {
			searchHistoryRepository.delete(histories.get(histories.size() - 1));
		}
	}

	private void saveNewHistory(Long userId, String keyword) {
		searchHistoryRepository.save(
			PostSearchHistory.builder()
				.userId(userId)
				.keyword(keyword)
				.build()
		);
	}
}