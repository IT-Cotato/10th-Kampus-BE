package com.cotato.kampus.domain.notice.domain;

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
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", columnDefinition = "text", nullable = false)
	private String content;

	@Builder
	public Notice(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
