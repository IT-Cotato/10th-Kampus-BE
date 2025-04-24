package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.PostScrap;

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
@Table(name = "post_scrap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_scrap_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public PostScrap toDomain() {
		return PostScrap.builder()
			.id(id)
			.userId(userId)
			.postId(postId)
			.build();
	}

	public static PostScrapEntity fromDomain(PostScrap postScrap) {
		PostScrapEntity entity = new PostScrapEntity();
		entity.id = postScrap.getId();
		entity.userId = postScrap.getUserId();
		entity.postId = postScrap.getPostId();
		return entity;
	}
}