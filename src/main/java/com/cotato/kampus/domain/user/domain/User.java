package com.cotato.kampus.domain.user.domain;

import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;

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
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "unique_id", nullable = false, unique = true)
	private String uniqueId;

	@Column(name = "provider_id", nullable = false)
	private String providerId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "university_id")
	private Long universityId;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "nationality", nullable = false)
	@Enumerated(EnumType.STRING)
	private Nationality nationality;

	@Column(name = "preferred_language", nullable = false)
	@Enumerated(EnumType.STRING)
	private PreferredLanguage preferredLanguage = PreferredLanguage.ENGLISH_AMERICAN;

	@Column(name = "user_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole = UserRole.UNVERIFIED;

	@Builder
	public User(String email, String uniqueId, String providerId, String username, String nickname, Long universityId,
		Nationality nationality, PreferredLanguage preferredLanguage, UserRole userRole) {
		this.email = email;
		this.uniqueId = uniqueId;
		this.providerId = providerId;
		this.username = username;
		this.nickname = nickname;
		this.universityId = universityId;
		this.nationality = nationality;
		this.preferredLanguage = preferredLanguage;
		this.userRole = userRole;
	}

	public User update(String email, String username) {
		this.email = email;
		this.username = username;
		return this;
	}

	public User updateDetails(String nickname, Nationality nationality, PreferredLanguage preferredLanguage) {
		this.nickname = nickname;
		this.nationality = nationality;
		this.preferredLanguage = preferredLanguage;
		return this;
	}
}
