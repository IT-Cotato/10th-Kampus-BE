package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardFavorite;

@Repository
public interface BoardFavoriteRepository extends JpaRepository<BoardFavorite, Long> {

	List<BoardFavorite> findAllByUserId(Long userId);

	boolean existsByUserIdAndBoardId(Long userId, Long boardId);

	Optional<BoardFavorite> findByUserIdAndBoardId(Long userId, Long boardId);
}
