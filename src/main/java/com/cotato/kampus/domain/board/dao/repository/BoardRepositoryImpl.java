package com.cotato.kampus.domain.board.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

	private final BoardJpaRepository boardJpaRepository;

	@Override
	public List<BoardEntity> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status){
		return boardJpaRepository.findAllByUniversityIdIsNullAndBoardStatus(status);
	}

	@Override
	public Optional<BoardEntity> findByUniversityId(Long universityId){
		return boardJpaRepository.findByUniversityId(universityId);
	}

	@Override
	public boolean existsByUniversityId(Long universityId){
		return boardJpaRepository.existsByUniversityId(universityId);
	}

	@Override
	public boolean existsByBoardName(String boardName){
		return boardJpaRepository.existsByBoardName(boardName);
	}

	@Override
	public Optional<BoardEntity> findByBoardType(BoardType boardType){
		return boardJpaRepository.findByBoardType(boardType);
	}

	@Override
	public List<BoardEntity> findByDeletionScheduledAtBefore(LocalDateTime now){
		return boardJpaRepository.findByDeletionScheduledAtBefore(now);
	}

	@Override
	public List<BoardEntity> findAllByBoardStatus(BoardStatus boardStatus){
		return boardJpaRepository.findAllByBoardStatus(boardStatus);
	}
}
