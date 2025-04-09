package com.cotato.kampus.domain.board.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false)
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Builder
	public BoardCategory(String categoryName, Long boardId) {
		this.categoryName = categoryName;
		this.boardId = boardId;
	}
}
