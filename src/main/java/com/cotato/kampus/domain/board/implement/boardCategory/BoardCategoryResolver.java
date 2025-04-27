package com.cotato.kampus.domain.board.implement.boardCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryResolver {

	private final BoardCategoryRepository boardCategoryRepository;

	public void validateMatching(List<Long> categoryIds, Long boardId) {
		List<Long> boardCategoryIds = boardCategoryRepository.findAllCategoryIdByBoardId(boardId);

		boolean allMatch = categoryIds.stream()
			.allMatch(boardCategoryIds::contains);

		if (!allMatch) {
			throw new AppException(ErrorCode.CATEGORY_NOT_BELONG_TO_BOARD);
		}
	}

	public void validateMatching(Long categoryId, Long boardId) {
		boolean match = boardCategoryRepository.existsByCategoryIdAndBoardId(categoryId, boardId);

		if (!match) {
			throw new AppException(ErrorCode.CATEGORY_NOT_BELONG_TO_BOARD);
		}
	}
}
