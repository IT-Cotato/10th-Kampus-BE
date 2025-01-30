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
@Table(name = "post_draft_photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDraftPhoto extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_draft_photo_id")
	private Long id;

	@Column(name = "post_draft_id", nullable = false)
	private Long postDraftId;

	@Column(name = "photo_url", nullable = false)
	private String photoUrl;

	@Builder
	public PostDraftPhoto(Long postDraftId, String photoUrl) {
		this.postDraftId = postDraftId;
		this.photoUrl = photoUrl;
	}
}
