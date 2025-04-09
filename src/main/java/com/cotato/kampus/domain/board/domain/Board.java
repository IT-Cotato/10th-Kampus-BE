package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Board {

	private final Long id;
	private final String boardName;
	private final String description;
	private final Long universityId;
	private final Boolean usesCategories;
	private final BoardStatus boardStatus;
	private final BoardType boardType;
	private final LocalDateTime deletionScheduledAt;
}