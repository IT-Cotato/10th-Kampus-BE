package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id", nullable = false)
	private Long id;

	@Column(name = "board_name", nullable = false)
	private String boardName;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "university_id")
	private Long universityId;

	@Column(name = "is_category_required", nullable = false)
	private Boolean isCategoryRequired;

	@Enumerated(EnumType.STRING)
	@Column(name = "board_status")
	private BoardStatus boardStatus;

	@Builder
	public Board(String boardName, String description, Long universityId, Boolean isCategoryRequired, BoardStatus boardStatus) {
		this.boardName = boardName;
		this.description = description;
		this.universityId = universityId;
		this.isCategoryRequired = isCategoryRequired;
		this.boardStatus = boardStatus;
	}

	public void update(String boardName, String description, Boolean isCategoryRequired) {
		this.boardName = boardName;
		this.description = description;
		this.isCategoryRequired = isCategoryRequired;
	}

	public void active(){
		this.boardStatus = BoardStatus.ACTIVE;
	}

	public void inactive(){
		this.boardStatus = BoardStatus.INACTIVE;
	}
}
