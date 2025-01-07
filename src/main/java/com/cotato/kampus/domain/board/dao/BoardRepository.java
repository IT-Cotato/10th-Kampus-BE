package com.cotato.kampus.domain.board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
