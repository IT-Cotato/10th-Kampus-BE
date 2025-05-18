package com.cotato.kampus.domain.product.enums;

import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSortType {
	recent("bumpedTime", Sort.Direction.DESC), // 최신순(끌어올린 시간)
	old("bumpedTime", Sort.Direction.ASC), // 오래된순(끌어올린 시간)
	scrapCount("scrapCount", Sort.Direction.DESC); // 스크랩 많은 순

	private final String property;
	private final Sort.Direction direction;
}
