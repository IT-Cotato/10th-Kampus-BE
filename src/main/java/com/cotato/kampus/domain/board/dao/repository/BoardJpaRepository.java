package com.cotato.kampus.domain.board.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

	List<BoardEntity> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status);

	Optional<BoardEntity> findByUniversityId(Long universityId);

	List<BoardEntity> findAllByIdIn(List<Long> ids);

	boolean existsByUniversityId(Long universityId);

	boolean existsByBoardName(String boardName);

	Optional<BoardEntity> findByBoardType(BoardType boardType);

	List<BoardEntity> findByDeletionScheduledAtBefore(LocalDateTime now);

	List<BoardEntity> findAllByBoardStatus(BoardStatus boardStatus);

	boolean existsByBoardType(BoardType boardType);

	@Query("SELECT b.id FROM BoardEntity b WHERE b.boardType IN :boardTypes")
	List<Long> findBoardIdsByBoardTypeIn(@Param("boardTypes") List<BoardType> boardTypes);
}
