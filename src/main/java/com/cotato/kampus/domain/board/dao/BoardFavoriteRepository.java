package com.cotato.kampus.domain.board.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.domain.BoardFavorite;

@Repository
public interface BoardFavoriteRepository extends JpaRepository<BoardFavorite, Long> {

	List<BoardFavorite> findAllByUserId(Long userId);
}
