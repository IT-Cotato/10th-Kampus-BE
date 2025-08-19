package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.board.dao.entity.BoardFavoriteEntity;

public interface BoardFavoriteJpaRepository extends JpaRepository<BoardFavoriteEntity, Long> {

	List<BoardFavoriteEntity> findAllByUserId(Long userId);

	boolean existsByUserIdAndBoardId(Long userId, Long boardId);

	Optional<BoardFavoriteEntity> findByUserIdAndBoardId(Long userId, Long boardId);

	void deleteByUserIdAndBoardId(Long userId, Long boardId);

	@Query("SELECT bf.boardId FROM BoardFavoriteEntity bf WHERE bf.userId = :userId")
	Set<Long> findBoardIdsByUserId(@Param("userId") Long userId);
}
