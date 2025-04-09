package com.cotato.kampus.domain.board.implement;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.repository.BoardCategoryRepository;
import com.cotato.kampus.domain.board.dao.entity.BoardCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryAppender {

	private final BoardCategoryRepository boardCategoryRepository;

	@Transactional
	public void appendCategories(Long boardId, List<String> categories) {
		List<BoardCategory> boardCategoryList = categories.stream()
			.map(categoryName -> BoardCategory.builder()
				.categoryName(categoryName)
				.boardId(boardId)
				.build())
			.toList();

		boardCategoryRepository.saveAll(boardCategoryList);
	}
}
