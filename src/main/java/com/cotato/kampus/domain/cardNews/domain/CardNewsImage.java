package com.cotato.kampus.domain.cardNews.domain;

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
@Table(name = "cardnews_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardNewsImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cardnews_photo_id")
	private Long id;

	@Column(name = "cardnews_id", nullable = false)
	private Long cardNewsId;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@Builder
	public CardNewsImage(Long cardNewsId, String imageUrl) {
		this.cardNewsId = cardNewsId;
		this.imageUrl = imageUrl;
	}
}
