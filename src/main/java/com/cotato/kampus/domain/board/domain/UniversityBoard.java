package com.cotato.kampus.domain.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "university_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityBoard extends Board {

	@Column(name = "university_id", nullable = false)
	private Long universityId;

	@Builder
	public UniversityBoard(Long universityId, String boardName) {
		super(boardName);
		this.universityId = universityId;
	}
}
