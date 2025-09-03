package com.cotato.kampus.domain.board.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.dao.projection.BoardWithPostCountProjection;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

	List<BoardEntity> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status);

	Optional<BoardEntity> findByUniversityId(Long universityId);

	List<BoardEntity> findAllByIdIn(Set<Long> ids);

	boolean existsByUniversityId(Long universityId);

	boolean existsByBoardName(String boardName);

	Optional<BoardEntity> findByBoardType(BoardType boardType);

	List<BoardEntity> findByDeletionScheduledAtBefore(LocalDateTime now);

	boolean existsByBoardType(BoardType boardType);

	@Query("SELECT b.id FROM BoardEntity b WHERE b.boardType IN :boardTypes")
	List<Long> findBoardIdsByBoardTypeIn(@Param("boardTypes") List<BoardType> boardTypes);

	@Query("SELECT b.id FROM BoardEntity b WHERE b.universityId = :universityId")
	Optional<Long> findBoardIdByUniversityId(@Param("universityId") Long universityId);

	@Query("SELECT b.boardType FROM BoardEntity b WHERE b.id = :boardId")
	Optional<String> findBoardTypeByBoardId(@Param("boardId") Long boardId);

	@Query("SELECT b AS board, COUNT(p) AS postCount FROM BoardEntity b LEFT JOIN PostEntity p ON b.id = p.boardId WHERE b.boardStatus = :status GROUP BY b")
	List<BoardWithPostCountProjection> findBoardsWithPostCount(@Param("status") BoardStatus status);

	@Query("SELECT b AS board, COUNT(p) AS postCount FROM BoardEntity b LEFT JOIN PostEntity p ON b.id = p.boardId GROUP BY b")
	List<BoardWithPostCountProjection> findAllBoardsWithPostCount();
}
