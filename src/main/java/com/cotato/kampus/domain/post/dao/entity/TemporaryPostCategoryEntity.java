package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.TemporaryPostCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "temp_post_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryPostCategoryEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "temp_post_category_id")
	private Long id;

	@Column(name = "temp_post_id", nullable = false)
	private Long temporaryPostId;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	public static TemporaryPostCategoryEntity fromDomain(TemporaryPostCategory temporaryPostCategory) {
		TemporaryPostCategoryEntity result = new TemporaryPostCategoryEntity();
		result.id = temporaryPostCategory.getId();
		result.temporaryPostId = temporaryPostCategory.getTemporaryPostId();
		result.categoryId = temporaryPostCategory.getCategoryId();
		return result;
	}

	public TemporaryPostCategory toDomain() {
		return TemporaryPostCategory.builder()
			.id(id)
			.temporaryPostId(temporaryPostId)
			.categoryId(categoryId)
			.build();
	}
}
