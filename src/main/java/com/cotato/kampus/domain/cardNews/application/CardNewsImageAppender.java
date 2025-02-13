package com.cotato.kampus.domain.cardNews.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.cardNews.dao.CardNewsImageRepository;
import com.cotato.kampus.domain.cardNews.domain.CardNewsImage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class CardNewsImageAppender {

	private final CardNewsImageRepository cardNewsImageRepository;

	public void appendAll(Long cardNewsId, List<String> imageUrls){
		imageUrls.forEach(imageUrl -> {
			CardNewsImage cardNewsImage = CardNewsImage.builder()
				.cardNewsId(cardNewsId)
				.imageUrl(imageUrl)
				.build();

			cardNewsImageRepository.save(cardNewsImage);
		});
	}
}
