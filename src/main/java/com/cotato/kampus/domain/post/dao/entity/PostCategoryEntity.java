package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.PostCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategoryEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_category_id")
	private Long id;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	public static PostCategoryEntity fromDomain(PostCategory postCategory) {
		PostCategoryEntity result = new PostCategoryEntity();
		result.id = postCategory.getId();
		result.postId = postCategory.getPostId();
		result.categoryId = postCategory.getCategoryId();
		return result;
	}

	public PostCategory toDomain() {
		return PostCategory.builder()
			.id(this.id)
			.postId(this.postId)
			.categoryId(this.categoryId)
			.build();
	}
}
