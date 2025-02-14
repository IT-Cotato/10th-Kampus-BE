package com.cotato.kampus.domain.board.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardType;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findAllByUniversityIdIsNull();

	Optional<Board> findByUniversityId(Long universityId);

	boolean existsByUniversityId(Long universityId);

	boolean existsByBoardName(String boardName);

	Optional<Board> findByBoardType(BoardType boardType);
}
