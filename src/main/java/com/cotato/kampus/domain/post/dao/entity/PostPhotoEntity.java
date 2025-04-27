package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.PostPhoto;

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
@Table(name = "post_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhotoEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_photo_id")
	private Long id;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "photo_url", nullable = false)
	private String photoUrl;

	@Column(name = "photo_order", nullable = false)
	private int order;

	public PostPhoto toDomain() {
		return PostPhoto.builder()
			.id(id)
			.postId(postId)
			.photoUrl(photoUrl)
			.order(order)
			.build();
	}

	public static PostPhotoEntity fromDomain(PostPhoto postPhoto) {
		PostPhotoEntity entity = new PostPhotoEntity();
		entity.id = postPhoto.getId();
		entity.postId = postPhoto.getPostId();
		entity.photoUrl = postPhoto.getPhotoUrl();
		entity.order = postPhoto.getOrder();
		return entity;
	}
}
