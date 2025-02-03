package com.cotato.kampus.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.user.domain.VerificationPhoto;

public interface VerificationPhotoRepository extends JpaRepository<VerificationPhoto, Long> {
}
