package com.cotato.kampus.domain.user.domain;

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

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Builder
	public VerificationPhoto(Long userId) {
		this.userId = userId;
	}
}
