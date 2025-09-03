package com.cotato.kampus.domain.board.dao.projection;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;

public interface BoardWithPostCountProjection {
	BoardEntity getBoard();
	Long getPostCount();
}