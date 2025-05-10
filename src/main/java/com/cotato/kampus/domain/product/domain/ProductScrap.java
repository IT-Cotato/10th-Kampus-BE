package com.cotato.kampus.domain.product.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductScrap {

	private Long id;
	private Long productId;
	private Long userId;
}
