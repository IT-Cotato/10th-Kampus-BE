package com.cotato.kampus.domain.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityBoard extends Board {

	@Column(name = "university_id", nullable = false)
	private Long universityId;

	public UniversityBoard(Long universityId, String boardName) {
		super(boardName);
		this.universityId = universityId;
	}
}