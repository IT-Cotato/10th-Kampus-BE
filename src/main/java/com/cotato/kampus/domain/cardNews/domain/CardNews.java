package com.cotato.kampus.domain.cardNews.domain;

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
@Table(name = "card_news")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardNews extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_news_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "likes", nullable = false)
	private Long likes = 0L;

	@Column(name = "scraps", nullable = false)
	private Long scraps = 0L;

	@Column(name = "comments", nullable = false)
	private Long comments = 0L;

	@Column(name = "next_ananymous_number", nullable = false)
	private Long nextAnonymousNumber = 1L;

	@Builder
	public CardNews(Long userId, String title, Long likes, Long scraps, Long comments, Long nextAnonymousNumber) {
		this.userId = userId;
		this.title = title;
		this.likes = likes;
		this.scraps = scraps;
		this.comments = comments;
		this.nextAnonymousNumber = nextAnonymousNumber;
	}
}
