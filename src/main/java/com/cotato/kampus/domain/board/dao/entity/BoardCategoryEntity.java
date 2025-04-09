package com.cotato.kampus.domain.board.dao.entity;

import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	public static BoardCategoryEntity fromDomain(BoardCategory boardCategory) {
		BoardCategoryEntity result = new BoardCategoryEntity();
		result.id = boardCategory.getId();
		result.categoryName = boardCategory.getCategoryName();
		result.boardId = boardCategory.getBoardId();
		return result;
	}

	public BoardCategory toDomain() {
		return BoardCategory.builder()
			.id(this.id)
			.categoryName(this.categoryName)
			.boardId(this.boardId)
			.build();
	}
}
