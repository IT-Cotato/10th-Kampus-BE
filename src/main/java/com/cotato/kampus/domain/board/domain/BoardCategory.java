package com.cotato.kampus.domain.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class BoardCategory {

	private final Long id;
	private final String categoryName;
	private final Long boardId;
}
