package com.cotato.kampus.domain.board.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardCategory {

	private final Long id;
	private final Long categoryId;
	private final Long boardId;
}
