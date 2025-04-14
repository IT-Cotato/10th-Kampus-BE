package com.cotato.kampus.domain.post.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.post.domain.PostSearchHistory;
import com.cotato.kampus.domain.post.dao.repository.PostSearchHistoryRepository;
import com.cotato.kampus.global.config.JpaAuditingConfig;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class PostSearchHistoryRepositoryTest {

	@Autowired
	PostSearchHistoryRepository postSearchHistoryRepository;

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("검색 기록 삭제 성공")
	void deleteAllByUserId() {
		PostSearchHistory keyword1 = PostSearchHistory.builder()
			.userId(1L)
			.keyword("keyword1")
			.build();

		PostSearchHistory keyword2 = PostSearchHistory.builder()
			.userId(1L)
			.keyword("keyword2")
			.build();

		PostSearchHistory keyword3 = PostSearchHistory.builder()
			.userId(1L)
			.keyword("keyword3")
			.build();

		postSearchHistoryRepository.save(keyword1);
		postSearchHistoryRepository.save(keyword2);
		postSearchHistoryRepository.save(keyword3);

		postSearchHistoryRepository.deleteAllByUserId(1L);
		assertThat(postSearchHistoryRepository.findByUserIdOrderByCreatedTimeDesc(1L)).isEmpty();
	}

	@Test
	@DisplayName("검색 기록 삭제 데이터 없어도 성공")
	void deleteAllByUserIdFail() {
		postSearchHistoryRepository.deleteAllByUserId(1L);
		assertThat(postSearchHistoryRepository.findByUserIdOrderByCreatedTimeDesc(1L)).isEmpty();
	}
}