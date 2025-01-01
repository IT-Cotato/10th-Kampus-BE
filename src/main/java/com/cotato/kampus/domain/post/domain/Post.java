package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.PostStatus;

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
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "text")
	private String content;

	@Column(name = "likes", nullable = false)
	private Long likes = 0L;

	@Column(name = "scraps", nullable = false)
	private Long scraps = 0L;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_status", nullable = false)
	private PostStatus postStatus;

	@Column(name = "post_category", nullable = false)
	private String postCategory;

	@Builder
	public Post(Long userId, Long boardId, String title, String content,
		Long likes, Long scraps, Anonymity anonymity,
		PostStatus postStatus, String postCategory) {
		this.userId = userId;
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.likes = likes;
		this.scraps = scraps;
		this.anonymity = anonymity;
		this.postStatus = postStatus;
		this.postCategory = postCategory;
	}
}
