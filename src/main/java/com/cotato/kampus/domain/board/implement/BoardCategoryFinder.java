package com.cotato.kampus.domain.board.implement;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.repository.BoardCategoryRepository;
import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;
import com.cotato.kampus.domain.board.domain.BoardCategoryDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryFinder {

	private final BoardCategoryRepository boardCategoryRepository;

	public List<BoardCategoryDto> findAllDtoByBoardId(Long boardId) {

		List<BoardCategoryEntity> categoryList = boardCategoryRepository.findAllByBoardId(boardId);

		return categoryList.stream()
			.map(BoardCategoryDto::from)
			.toList();
	}

	public BoardCategoryDto findDto(Long boardId, String categoryName) {
		BoardCategoryEntity category = boardCategoryRepository.findByBoardIdAndCategoryName(boardId, categoryName)
			.orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY));

		return BoardCategoryDto.from(category);
	}
}
