package com.cotato.kampus.domain.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.user.domain.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
	Optional<Agreement> findByUserId(Long userId);
}