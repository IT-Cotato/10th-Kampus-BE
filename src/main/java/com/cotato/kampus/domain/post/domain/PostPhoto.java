package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

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
@Table(name = "post_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_photo_id")
	private Long id;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "photo_url", nullable = false)
	private String photoUrl;

	@Builder
	public PostPhoto(Long postId, String photoUrl) {
		this.postId = postId;
		this.photoUrl = photoUrl;
	}
}
