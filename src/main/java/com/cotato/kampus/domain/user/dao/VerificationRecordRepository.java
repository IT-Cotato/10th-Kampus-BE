package com.cotato.kampus.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.user.domain.VerificationRecord;

public interface VerificationRecordRepository extends JpaRepository<VerificationRecord, Long> {
}
