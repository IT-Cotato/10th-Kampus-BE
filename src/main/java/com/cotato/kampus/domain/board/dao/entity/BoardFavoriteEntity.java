package com.cotato.kampus.domain.board.dao.entity;

import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_favorite")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_favorite_id")
	private Long id;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public static BoardFavoriteEntity fromDomain(BoardFavorite boardFavorite) {
		BoardFavoriteEntity result = new BoardFavoriteEntity();
		result.id = boardFavorite.getId();
		result.boardId = boardFavorite.getBoardId();
		result.userId = boardFavorite.getUserId();
		return result;
	}

	public BoardFavorite toDomain() {
		return BoardFavorite.builder()
			.id(this.id)
			.boardId(this.boardId)
			.userId(this.userId)
			.build();
	}
}
