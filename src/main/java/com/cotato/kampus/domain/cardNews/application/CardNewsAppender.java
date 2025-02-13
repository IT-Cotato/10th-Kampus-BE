package com.cotato.kampus.domain.cardNews.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.cardNews.dao.CardNewsRepository;
import com.cotato.kampus.domain.cardNews.domain.CardNews;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional
public class CardNewsAppender {

	private final CardNewsRepository cardNewsRepository;

	public Long append(Long userId, String title){
		CardNews cardNews = CardNews.builder()
			.userId(userId)
			.title(title)
			.likes(0L)
			.comments(0L)
			.scraps(0L)
			.nextAnonymousNumber(1L)
			.build();

		return cardNewsRepository.save(cardNews).getId();
	}
}
