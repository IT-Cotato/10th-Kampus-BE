package com.cotato.kampus.domain.board.implement.boardCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryAppender {

	private final BoardCategoryRepository boardCategoryRepository;

	@Transactional
	public void appendCategories(Long boardId, List<Long> categories) {
		List<BoardCategory> boardCategoryList = categories.stream()
			.map(categoryId -> BoardCategory.builder()
				.categoryId(categoryId)
				.boardId(boardId)
				.build())
			.toList();

		boardCategoryRepository.saveAll(boardCategoryList);
	}
}
