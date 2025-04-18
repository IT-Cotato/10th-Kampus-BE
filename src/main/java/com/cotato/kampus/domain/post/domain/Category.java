package com.cotato.kampus.domain.post.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {

	private final Long id;
	private final String categoryName;
}
