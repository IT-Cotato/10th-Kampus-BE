package com.cotato.kampus.domain.verification.dao;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;
import com.cotato.kampus.domain.verification.domain.VerificationRecord;

public interface VerificationRecordRepository extends JpaRepository<VerificationRecord, Long> {

	Slice<VerificationRecord> findAllByOrderByCreatedTimeDesc(Pageable pageable);

	Optional<VerificationRecord> findByUserId(Long userId);

	void deleteAllByUserId(Long userId);

	@Query("SELECT v FROM VerificationRecord v "
		+ "WHERE v.userId = :userId "
		+ "AND v.verificationType = :type "
		+ "AND v.verificationStatus = :status")
	Optional<VerificationRecord> findLatestByUserIdAndTypeAndStatus(Long userId, VerificationType type, VerificationStatus status);
}
