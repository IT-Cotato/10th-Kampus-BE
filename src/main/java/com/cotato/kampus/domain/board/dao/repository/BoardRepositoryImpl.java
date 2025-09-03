package com.cotato.kampus.domain.board.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

	private final BoardJpaRepository boardJpaRepository;

	@Override
	public Board save(Board board) {
		return boardJpaRepository.save(BoardEntity.fromDomain(board)).toDomain();
	}

	@Override
	public List<Board> findAll() {
		return boardJpaRepository.findAll()
			.stream()
			.map(BoardEntity::toDomain)
			.toList();
	}

	@Override
	public Optional<Board> findById(Long id){
		return boardJpaRepository.findById(id)
			.map(BoardEntity::toDomain);
	}

	@Override
	public List<Board> findAllByIdIn(Set<Long> ids){
		return boardJpaRepository.findAllByIdIn(ids).stream()
			.map(BoardEntity::toDomain)
			.toList();
	}

	@Override
	public void deleteAll(List<Board> boards) {
		List<BoardEntity> boardEntities = boards.stream()
			.map(BoardEntity::fromDomain)
			.toList();
		boardJpaRepository.deleteAll(boardEntities);
	}

	@Override
	public List<Board> findAllByUniversityIdIsNullAndBoardStatus(BoardStatus status){
		return boardJpaRepository.findAllByUniversityIdIsNullAndBoardStatus(status)
			.stream()
			.map(BoardEntity::toDomain)
			.toList();
	}

	@Override
	public Optional<Board> findByUniversityId(Long universityId){
		return boardJpaRepository.findByUniversityId(universityId)
			.map(BoardEntity::toDomain);
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
	public Optional<Board> findByBoardType(BoardType boardType){
		return boardJpaRepository.findByBoardType(boardType)
			.map(BoardEntity::toDomain);
	}

	@Override
	public List<Board> findByDeletionScheduledAtBefore(LocalDateTime now){
		return boardJpaRepository.findByDeletionScheduledAtBefore(now)
			.stream()
			.map(BoardEntity::toDomain)
			.toList();
	}

	@Override
	public boolean existsByBoardType(BoardType boardType) {
		return boardJpaRepository.existsByBoardType(boardType);
	}

	@Override
	public List<Long> findBoardIdsByBoardTypeIn(List<BoardType> boardTypes) {
		return boardJpaRepository.findBoardIdsByBoardTypeIn(boardTypes);
	}

	@Override
	public Optional<Long> findBoardIdByUniversityId(Long universityId) {
		return boardJpaRepository.findBoardIdByUniversityId(universityId);
	}

	@Override
	public Optional<String> findBoardTypeByBoardId(Long boardId) {
		return boardJpaRepository.findBoardTypeByBoardId(boardId);
	}

	@Override
	public List<Object[]> findBoardsWithPostCount(BoardStatus status) {
		List<Object[]> results = boardJpaRepository.findBoardsWithPostCount(status);
		return results.stream()
			.map(arr -> new Object[] {
				((BoardEntity) arr[0]).toDomain(),  // BoardEntity -> Board 변환
				arr[1]  // 게시글 수는 그대로
			})
			.toList();
	}

	@Override
	public List<Object[]> findAllBoardsWithPostCount() {
		List<Object[]> results = boardJpaRepository.findAllBoardsWithPostCount();
		return results.stream()
			.map(arr -> new Object[] {
				((BoardEntity) arr[0]).toDomain(),  // BoardEntity -> Board 변환
				arr[1]  // 게시글 수는 그대로
			})
			.toList();
	}
}
