package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDraftCategory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_draft_category_id")
	private Long id;

	@Column(name = "post_draft_id", nullable = false)
	private Long postDraftId;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Builder
	public PostDraftCategory(Long postDraftId, Long categoryId) {
		this.postDraftId = postDraftId;
		this.categoryId = categoryId;
	}
}
