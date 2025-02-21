package com.cotato.kampus.domain.verification.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verification_record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationRecord extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "verification_record_id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "university_id")
	private Long universityId;

	@Column(name = "verification_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private VerificationType verificationType;

	@Column(name = "verification_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private VerificationStatus verificationStatus;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	@Builder
	public VerificationRecord(Long userId, Long universityId, VerificationType verificationType,
		VerificationStatus verificationStatus) {
		this.userId = userId;
		this.universityId = universityId;
		this.verificationType = verificationType;
		this.verificationStatus = verificationStatus;
	}

	public void setStatus(VerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

}
