package com.cotato.kampus.domain.board.dao.entity;

import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private Long id;

	@Column(name = "category_id", nullable = false)
	private Long categoryid;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	public static BoardCategoryEntity fromDomain(BoardCategory boardCategory) {
		BoardCategoryEntity result = new BoardCategoryEntity();
		result.id = boardCategory.getId();
		result.categoryid = boardCategory.getCategoryId();
		result.boardId = boardCategory.getBoardId();
		return result;
	}

	public BoardCategory toDomain() {
		return BoardCategory.builder()
			.id(this.id)
			.categoryId(this.categoryid)
			.boardId(this.boardId)
			.build();
	}
}
