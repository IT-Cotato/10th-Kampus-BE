package com.cotato.kampus.domain.user.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.user.domain.VerificationRecord;

public interface VerificationRecordRepository extends JpaRepository<VerificationRecord, Long> {

	Slice<VerificationRecord> findAllByOrderByCreatedTimeDesc(Pageable pageable);
}
