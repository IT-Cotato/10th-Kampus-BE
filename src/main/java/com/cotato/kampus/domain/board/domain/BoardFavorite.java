package com.cotato.kampus.domain.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class BoardFavorite {

	private final Long id;
	private final Long boardId;
	private final Long userId;
}
