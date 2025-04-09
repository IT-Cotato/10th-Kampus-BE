package com.cotato.kampus.domain.board.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardFavoriteEntity;

@Repository
public interface BoardFavoriteRepository extends JpaRepository<BoardFavoriteEntity, Long> {

	List<BoardFavoriteEntity> findAllByUserId(Long userId);

	boolean existsByUserIdAndBoardId(Long userId, Long boardId);

	Optional<BoardFavoriteEntity> findByUserIdAndBoardId(Long userId, Long boardId);
}
