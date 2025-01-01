package com.cotato.kampus.domain.university.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;

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
@Table(name = "university")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "university_id")
	private Long id;

	@Column(name = "university_name", length = 10)
	private String universityName;

	@Builder
	public University(String universityName) {
		this.universityName = universityName;
	}
}
