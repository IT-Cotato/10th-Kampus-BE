package com.cotato.kampus.domain.product.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.product.domain.ProductPhoto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPhotoEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_photo_id")
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "photo_url", nullable = false)
	private String photoUrl;

	@Column(name = "photo_order", nullable = false)
	private Integer order;

	public ProductPhoto toDomain() {
		return ProductPhoto.builder()
			.id(getId())
			.productId(getProductId())
			.photoUrl(getPhotoUrl())
			.order(getOrder())
			.build();
	}

	public static ProductPhotoEntity fromDomain(ProductPhoto productPhoto) {
		ProductPhotoEntity entity = new ProductPhotoEntity();
		entity.id = productPhoto.getId();
		entity.productId = productPhoto.getProductId();
		entity.photoUrl = productPhoto.getPhotoUrl();
		entity.order = productPhoto.getOrder();
		return entity;
	}
}