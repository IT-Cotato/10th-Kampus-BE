package com.cotato.kampus.domain.board.implement;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;
import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryAppender {

	private final BoardCategoryRepository boardCategoryRepository;

	@Transactional
	public void appendCategories(Long boardId, List<String> categories) {
		List<BoardCategoryEntity> boardCategoryEntityList = categories.stream()
			.map(categoryName -> BoardCategoryEntity.builder()
				.categoryName(categoryName)
				.boardId(boardId)
				.build())
			.toList();

		boardCategoryRepository.saveAll(boardCategoryEntityList);
	}
}
