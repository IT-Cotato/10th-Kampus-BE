package com.cotato.kampus.domain.post.application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.post.dao.PostSearchHistoryRepository;

@ExtendWith(MockitoExtension.class)
class PostSearchHistoryDeleterTest {

	@Mock
	PostSearchHistoryRepository postSearchHistoryRepository;

	@InjectMocks
	PostSearchHistoryDeleter target;

	@Test
	@DisplayName("id로 검색 기록 삭제 성공")
	void deleteHistory() {
		// given
		Long id = 1L;
		doNothing().when(postSearchHistoryRepository).deleteById(id);

		// when
		target.deleteHistory(id);

		// then
		verify(postSearchHistoryRepository, times(1)).deleteById(id);
	}

	@Test
	@DisplayName("userId로 검색 기록 전체 삭제 성공")
	void deleteAllHistory() {
		// given
		Long userId = 1L;
		doNothing().when(postSearchHistoryRepository).deleteAllByUserId(userId);

		// when
		target.deleteAllHistory(userId);

		// then
		verify(postSearchHistoryRepository, times(1)).deleteAllByUserId(userId);
	}
}