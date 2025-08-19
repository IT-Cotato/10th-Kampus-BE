package com.cotato.kampus.domain.board.implement.port;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.domain.BoardFavorite;

@Repository
public interface BoardFavoriteRepository {

	BoardFavorite save(BoardFavorite boardFavorite);

	List<BoardFavorite> findAllByUserId(Long userId);

	boolean existsByUserIdAndBoardId(Long userId, Long boardId);

	Optional<BoardFavorite> findByUserIdAndBoardId(Long userId, Long boardId);

	void delete(BoardFavorite boardFavorite);

	void saveAll(List<BoardFavorite> boardFavorites);

	void deleteByUserIdAndBoardId(Long userId, Long boardId);

	Set<Long> findBoardIdsByUserId(Long userId);
}
