package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostSearchHistoryRepository;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryDto;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryList;

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
}