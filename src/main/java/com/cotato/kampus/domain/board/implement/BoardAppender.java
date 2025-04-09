package com.cotato.kampus.domain.board.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.dao.repository.BoardRepository;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAppender {
	private final BoardRepository boardRepository;

	@Transactional
	public Long appendBoard(String boardName, String description, Long universityId, Boolean usesCategories) {
		BoardType boardType = (universityId != null) ? BoardType.UNIVERSITY : BoardType.GENERAL;
		BoardEntity boardEntity = BoardEntity.builder()
			.boardName(boardName)
			.description(description)
			.boardType(boardType)
			.usesCategories(usesCategories)
			.boardStatus(BoardStatus.ACTIVE)
			.build();

		return boardRepository.save(boardEntity).getId();
	}
}
