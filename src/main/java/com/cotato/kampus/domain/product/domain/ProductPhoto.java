package com.cotato.kampus.domain.product.domain;

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
@Table(name = "product_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_photo_id")
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "product_photo_url", nullable = false)
	private String productPhotoUrl;

	@Column(name = "product_thumbnail_url", nullable = false)
	private String productThumbnailUrl;

	@Builder
	public ProductPhoto(Long productId, String productPhotoUrl, String productThumbnailUrl) {
		this.productId = productId;
		this.productPhotoUrl = productPhotoUrl;
		this.productThumbnailUrl = productThumbnailUrl;
	}
}
