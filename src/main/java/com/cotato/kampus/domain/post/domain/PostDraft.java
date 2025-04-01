package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.enums.PostCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_draft")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDraft extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_draft_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "title")
	private String title;

	@Column(name = "content", columnDefinition = "text")
	private String content;

	@Builder
	public PostDraft(Long userId, Long boardId, String title, String content) {
		this.userId = userId;
		this.boardId = boardId;
		this.title = title;
		this.content = content;
	}

	public void update(String title, String content){
		this.title = title;
		this.content = content;
	}
}
