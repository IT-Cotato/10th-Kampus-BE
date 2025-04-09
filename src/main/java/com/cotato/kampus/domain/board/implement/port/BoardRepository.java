package com.cotato.kampus.domain.board.implement.port;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	List<BoardEntity> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status);

	Optional<BoardEntity> findByUniversityId(Long universityId);

	boolean existsByUniversityId(Long universityId);

	boolean existsByBoardName(String boardName);

	Optional<BoardEntity> findByBoardType(BoardType boardType);

	List<BoardEntity> findByDeletionScheduledAtBefore(LocalDateTime now);

	List<BoardEntity> findAllByBoardStatus(BoardStatus boardStatus);
}
