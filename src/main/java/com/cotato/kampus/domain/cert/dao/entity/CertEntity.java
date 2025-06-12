package com.cotato.kampus.domain.cert.dao.entity;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cert_id")
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "univ_code", nullable = false)
	private String univCode;

	@Column(name = "code")
	private String code;

	@Column(name = "certified")
	protected boolean certified;

	@Column(name = "userId")
	private Long userId;

	@Column(name = "expiration_time")
	private LocalDateTime expirationTime;

	public Cert toDomain() {
		return Cert.fromEntity(id, email, univCode, code, certified, userId, expirationTime);
	}

	public static CertEntity fromDomain(Cert cert) {
		CertEntity certEntity = new CertEntity();
		certEntity.id = cert.getId();
		certEntity.email = cert.getEmail();
		certEntity.univCode = cert.getUnivCode();
		certEntity.code = cert.getCode();
		certEntity.certified = cert.isCertified();
		certEntity.userId = cert.getUserId();
		certEntity.expirationTime = cert.getExpirationTime();
		return certEntity;
	}
}
