package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.TemporaryPhoto;

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
@Table(name = "temp_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryPhotoEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "temp_photo_id")
	private Long id;

	@Column(name = "temp_post_id", nullable = false)
	private Long temporaryPostId;

	@Column(name = "temp_photo_url", nullable = false)
	private String photoUrl;

	@Column(name = "temp_photo_order", nullable = false)
	private int order;

	public TemporaryPhoto toDomain() {
		return TemporaryPhoto.builder()
			.id(id)
			.temporaryPostId(temporaryPostId)
			.photoUrl(photoUrl)
			.order(order)
			.build();
	}

	public static TemporaryPhotoEntity fromDomain(TemporaryPhoto temporaryPhoto) {
		TemporaryPhotoEntity entity = new TemporaryPhotoEntity();
		entity.id = temporaryPhoto.getId();
		entity.temporaryPostId = temporaryPhoto.getTemporaryPostId();
		entity.photoUrl = temporaryPhoto.getPhotoUrl();
		entity.order = temporaryPhoto.getOrder();
		return entity;
	}
}
