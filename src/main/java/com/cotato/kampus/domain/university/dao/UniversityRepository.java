package com.cotato.kampus.domain.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.university.domain.University;

public interface UniversityRepository extends JpaRepository<University, Long> {

	Optional<University> findByUniversityName(String name);
}
