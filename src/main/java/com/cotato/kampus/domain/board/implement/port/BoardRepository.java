package com.cotato.kampus.domain.board.implement.port;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public interface BoardRepository{

	Board save(Board board);

	List<Board> findAll();

	Optional<Board> findById(Long id);

	List<Board> findAllByIdIn(Set<Long> boardIds);

	void deleteAll(List<Board> boards);

	List<Board> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status);

	Optional<Board> findByUniversityId(Long universityId);

	boolean existsByUniversityId(Long universityId);

	boolean existsByBoardName(String boardName);

	Optional<Board> findByBoardType(BoardType boardType);

	List<Board> findByDeletionScheduledAtBefore(LocalDateTime now);

	boolean existsByBoardType(BoardType boardType);

	List<Long> findBoardIdsByBoardTypeIn(List<BoardType> boardTypes);

	Optional<Long> findBoardIdByUniversityId(Long universityId);

	Optional<String> findBoardTypeByBoardId(Long boardId);

	List<Object[]> findBoardsWithPostCount(BoardStatus status);

	List<Object[]> findAllBoardsWithPostCount();
}
