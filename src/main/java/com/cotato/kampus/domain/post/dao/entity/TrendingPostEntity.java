package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.TrendingPost;

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
@Table(name = "trending_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrendingPostEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trending_post_id")
	private Long id;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	public TrendingPost toDomain(){
		return TrendingPost.builder()
			.id(id)
			.postId(postId)
			.build();
	}

	public static TrendingPostEntity fromDomain(TrendingPost trendingPost) {
		TrendingPostEntity entity = new TrendingPostEntity();
		entity.id = trendingPost.getId();
		entity.postId = trendingPost.getPostId();
		return entity;
	}
}