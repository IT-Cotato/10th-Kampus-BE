package com.cotato.kampus.domain.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "refresh_entity")
public class RefreshEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "unique_id", nullable = false)
	private String uniqueId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "refresh", nullable = false, length = 2048)
	private String refresh;

	@Column(name = "expiration", nullable = false)
	private String expiration;

	@Builder
	public RefreshEntity(String uniqueId, String username, String refresh, String expiration) {
		this.uniqueId = uniqueId;
		this.username = username;
		this.refresh = refresh;
		this.expiration = expiration;
	}

}
