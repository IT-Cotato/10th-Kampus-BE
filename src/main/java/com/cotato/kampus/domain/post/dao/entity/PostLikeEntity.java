package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.PostLike;

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
@Table(name = "post_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_like_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public PostLike toDomain() {
		return PostLike.builder()
			.id(id)
			.userId(userId)
			.postId(postId)
			.build();
	}

	public static PostLikeEntity fromDomain(PostLike postLike) {
		PostLikeEntity entity = new PostLikeEntity();
		entity.id = postLike.getId();
		entity.userId = postLike.getUserId();
		entity.postId = postLike.getPostId();
		return entity;
	}
}
