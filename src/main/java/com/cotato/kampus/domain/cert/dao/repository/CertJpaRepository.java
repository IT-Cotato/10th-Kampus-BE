package com.cotato.kampus.domain.cert.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.cert.dao.entity.CertEntity;

public interface CertJpaRepository extends JpaRepository<CertEntity, Long> {

	Optional<CertEntity> findByEmail(String email);

	void deleteAllByUserId(Long userId);
}