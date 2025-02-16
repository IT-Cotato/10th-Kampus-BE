package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostSearchHistoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostSearchHistoryDeleter {

	private final PostSearchHistoryRepository postSearchHistoryRepository;

	@Transactional
	public void deleteHistory(Long id) {
		postSearchHistoryRepository.deleteById(id);
	}
}