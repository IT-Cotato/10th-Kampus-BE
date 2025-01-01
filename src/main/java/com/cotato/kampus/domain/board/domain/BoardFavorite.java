package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_favorite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavorite extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_favorite_id")
	private Long id;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Builder
	public BoardFavorite(Long boardId, Long userId) {
		this.boardId = boardId;
		this.userId = userId;
	}
}
