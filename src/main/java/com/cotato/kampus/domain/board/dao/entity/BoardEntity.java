package com.cotato.kampus.domain.board.dao.entity;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity extends BaseTimeEntity {

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

	public void update(String boardName, String description, Boolean usesCategories) {
		this.boardName = boardName;
		this.description = description;
		this.usesCategories = usesCategories;
	}

	public void updateStatus(BoardStatus boardStatus) {
		this.boardStatus = boardStatus;
	}

	public void setDeletionScheduledAt(LocalDateTime deletionScheduledAt) {
		this.deletionScheduledAt = deletionScheduledAt;
	}

	public static BoardEntity fromDomain(Board board) {
		BoardEntity result = new BoardEntity();
		result.id = board.getId();
		result.boardName = board.getBoardName();
		result.description = board.getDescription();
		result.universityId = board.getUniversityId();
		result.usesCategories = board.getUsesCategories();
		result.boardStatus = board.getBoardStatus();
		result.boardType = board.getBoardType();
		result.deletionScheduledAt = board.getDeletionScheduledAt();
		return result;
	}

	public Board toDomain() {
		return Board.builder()
			.id(this.id)
			.boardName(this.boardName)
			.description(this.description)
			.universityId(this.universityId)
			.usesCategories(this.usesCategories)
			.boardStatus(this.boardStatus)
			.boardType(this.boardType)
			.deletionScheduledAt(this.deletionScheduledAt)
			.build();
	}

}
