package com.cotato.kampus.domain.post.enums;

import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public enum PostSortType {
	recent("createdTime", Sort.Direction.DESC),    // 최신순
	old("createdTime", Sort.Direction.ASC),        // 오래된순
	likes("likes", Sort.Direction.DESC);           // 좋아요순

	private final String property;
	private final Sort.Direction direction;

	PostSortType(String property, Sort.Direction direction) {
		this.property = property;
		this.direction = direction;
	}

}