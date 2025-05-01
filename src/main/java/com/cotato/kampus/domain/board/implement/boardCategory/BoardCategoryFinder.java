package com.cotato.kampus.domain.board.implement.boardCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryFinder {

	private final BoardCategoryRepository boardCategoryRepository;

	public List<Long> findAllByBoardId(Long boardId) {
		return boardCategoryRepository.findAllCategoryIdByBoardId(boardId);
	}
}
