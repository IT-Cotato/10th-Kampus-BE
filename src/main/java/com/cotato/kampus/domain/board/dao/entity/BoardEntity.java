package com.cotato.kampus.domain.board.dao.entity;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.NormalBoard;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@Column(name = "board_name", nullable = false)
	private String boardName;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "university_id")
	private Long universityId;

	@Column(name = "uses_categories", nullable = false)
	private Boolean usesCategories;

	@Enumerated(EnumType.STRING)
	@Column(name = "board_status", nullable = false)
	private BoardStatus boardStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "board_type", nullable = false)
	private BoardType boardType;

	@Column(name = "deletion_scheduled_at")
	private LocalDateTime deletionScheduledAt;

	public static BoardEntity fromDomain(Board board) {
		BoardEntity result = new BoardEntity();
		result.id = board.getId();
		result.boardName = board.getBoardName();
		result.description = board.getDescription();
		result.usesCategories = board.getUsesCategories();
		result.boardStatus = board.getBoardStatus();
		result.boardType = board.getBoardType();
		result.deletionScheduledAt = board.getDeletionScheduledAt();

		if (board instanceof UniversityBoard) {
			result.universityId = ((UniversityBoard)board).getUniversityId();
		}

		return result;
	}

	public Board toDomain() {
		switch (boardType) {
			case NORMAL, CARDNEWS, FIXED, TRENDING:
				return NormalBoard.builder()
					.id(this.id)
					.boardName(this.boardName)
					.description(this.description)
					.usesCategories(this.usesCategories)
					.boardStatus(this.boardStatus)
					.boardType(this.boardType)
					.deletionScheduledAt(this.deletionScheduledAt)
					.build();

			case UNIVERSITY:
				return UniversityBoard.builder()
					.id(this.id)
					.boardName(this.boardName)
					.description(this.description)
					.usesCategories(this.usesCategories)
					.boardStatus(this.boardStatus)
					.universityId(this.universityId)
					.deletionScheduledAt(this.deletionScheduledAt)
					.build();

			default:
				throw new AppException(ErrorCode.INVALID_BOARD_TYPE);
		}
	}
}
