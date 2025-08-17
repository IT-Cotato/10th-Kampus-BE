package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.board.dao.entity.BoardFavoriteEntity;

public interface BoardFavoriteJpaRepository extends JpaRepository<BoardFavoriteEntity, Long> {

	List<BoardFavoriteEntity> findAllByUserId(Long userId);

	boolean existsByUserIdAndBoardId(Long userId, Long boardId);

	Optional<BoardFavoriteEntity> findByUserIdAndBoardId(Long userId, Long boardId);

	void deleteByUserIdAndBoardId(Long userId, Long boardId);
}
