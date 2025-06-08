package com.cotato.kampus.domain.cert.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.cert.domain.CertEntity;

public interface CertJpaRepository extends JpaRepository<CertEntity, Long> {

	Optional<CertEntity> findByEmail(String email);
}