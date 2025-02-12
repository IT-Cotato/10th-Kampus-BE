package com.cotato.kampus.domain.verification.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verification_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationPhoto extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "verification_photo_id")
	private Long id;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@Column(name = "verification_record_id", nullable = false)
	private Long verificationRecordId;


	@Builder
	public VerificationPhoto(String imageUrl, Long verificationRecordId) {
		this.imageUrl = imageUrl;
		this.verificationRecordId = verificationRecordId;
	}
}
