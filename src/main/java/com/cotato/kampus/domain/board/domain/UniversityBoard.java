package com.cotato.kampus.domain.board.domain;

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
@Table(name = "universirt_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityBoard extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "university_id", nullable = false)
	private Long universityId;

	@Column(name = "title", nullable = false)
	private String title;

	@Builder
	public UniversityBoard(Long universityId, String title) {
		this.universityId = universityId;
		this.title = title;
	}
}
