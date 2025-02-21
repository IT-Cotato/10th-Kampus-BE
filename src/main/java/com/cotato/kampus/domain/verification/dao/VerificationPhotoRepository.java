package com.cotato.kampus.domain.verification.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.verification.domain.VerificationPhoto;

public interface VerificationPhotoRepository extends JpaRepository<VerificationPhoto, Long> {
	Optional<VerificationPhoto> findByVerificationRecordId(Long verificationRecordId);
}
