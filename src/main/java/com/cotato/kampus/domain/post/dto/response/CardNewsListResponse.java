package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.CardNewsPreview;

public record CardNewsListResponse(
	List<CardNewsPreview> posts,
	Boolean hasNext
) {
	public static CardNewsListResponse from(Slice<CardNewsPreview> posts) {
		return new CardNewsListResponse(
			posts.getContent(),
			posts.hasNext()
		);
	}
}
