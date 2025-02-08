package com.cotato.kampus.domain.verification.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.verification.domain.VerificationPhoto;

public interface VerificationPhotoRepository extends JpaRepository<VerificationPhoto, Long> {
}
