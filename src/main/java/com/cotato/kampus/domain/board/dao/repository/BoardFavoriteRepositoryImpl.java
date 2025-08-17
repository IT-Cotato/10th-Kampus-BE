package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardFavoriteEntity;
import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardFavoriteRepositoryImpl implements BoardFavoriteRepository {

	private final BoardFavoriteJpaRepository boardFavoriteJpaRepository;

	@Override
	public BoardFavorite save(BoardFavorite boardFavorite) {
		BoardFavoriteEntity boardFavoriteEntity = BoardFavoriteEntity.fromDomain(boardFavorite);
		return boardFavoriteJpaRepository.save(boardFavoriteEntity).toDomain();
	}

	@Override
	public List<BoardFavorite> findAllByUserId(Long userId) {
		return boardFavoriteJpaRepository.findAllByUserId(userId).stream()
			.map(BoardFavoriteEntity::toDomain)
			.toList();
	}

	@Override
	public boolean existsByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteJpaRepository.existsByUserIdAndBoardId(userId, boardId);
	}

	@Override
	public Optional<BoardFavorite> findByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteJpaRepository.findByUserIdAndBoardId(userId, boardId)
			.map(BoardFavoriteEntity::toDomain);
	}

	@Override
	public void delete(BoardFavorite boardFavorite) {
		BoardFavoriteEntity boardFavoriteEntity = BoardFavoriteEntity.fromDomain(boardFavorite);
		boardFavoriteJpaRepository.delete(boardFavoriteEntity);
	}

	@Override
	public void saveAll(List<BoardFavorite> boardFavorites) {
		List<BoardFavoriteEntity> boardFavoriteEntities = boardFavorites.stream()
			.map(BoardFavoriteEntity::fromDomain)
			.toList();
		boardFavoriteJpaRepository.saveAll(boardFavoriteEntities);
	}

	@Override
	public void deleteByUserIdAndBoardId(Long userId, Long boardId) {
		boardFavoriteJpaRepository.deleteByUserIdAndBoardId(userId, boardId);
	}
}
