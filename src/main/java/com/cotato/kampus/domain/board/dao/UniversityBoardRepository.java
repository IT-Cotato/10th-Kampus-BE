package com.cotato.kampus.domain.board.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.board.domain.UniversityBoard;

public interface UniversityBoardRepository extends JpaRepository<UniversityBoard, Long> {
	Optional<UniversityBoard> findByUniversityId(Long universityId);
}
