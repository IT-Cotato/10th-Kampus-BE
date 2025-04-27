package com.cotato.kampus.domain.post.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemporaryPostCategory {

	private final Long id;
	private final Long categoryId;
	private final Long temporaryPostId;
}
