package com.cotato.kampus.domain.board.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public class TestBoardHelper {

	public static UniversityBoard createUniversityBoard(
		Long id,
		Boolean usesCategories,
		BoardStatus boardStatus,
		Long universityId
	) {
		return new UniversityBoard(
			id,
			"홍익대 게시판",
			"홍익대 게시판입니다.",
			usesCategories,
			boardStatus,
			BoardType.UNIVERSITY,
			universityId,
			LocalDateTime.now()
		);
	}

	public static NormalBoard createNormalBoard(
		Long id,
		Boolean usesCategories,
		BoardStatus boardStatus
	) {
		return new NormalBoard(
			id,
			"일반 게시팜",
			"일반 게시판입니다.",
			usesCategories,
			boardStatus,
			BoardType.NORMAL,
			LocalDateTime.now()
		);
	}
}
