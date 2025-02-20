package com.cotato.kampus.domain.post.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "title", length = 30, nullable = false)
	private String title;

	@Column(name = "content", length = 1000)
	private String content;

	@Column(name = "likes", nullable = false)
	@Builder.Default
	private Long likes = 0L;

	@Column(name = "scraps", nullable = false)
	@Builder.Default
	private Long scraps = 0L;

	@Column(name = "comments", nullable = false)
	@Builder.Default
	private Long comments = 0L;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_status", nullable = false)
	private PostStatus postStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_category")
	private PostCategory postCategory;

	@Column(name = "next_ananymous_number", nullable = false)
	@Builder.Default
	private Long nextAnonymousNumber = 1L;

	public void update(String title, String content, PostCategory postCategory) {
		this.title = title;
		this.content = content;
		this.postCategory = postCategory;
	}

	public void increaseNextAnonymousNumber() {
		this.nextAnonymousNumber++;
	}

	public void increaseScraps() {
		this.scraps++;
	}

	public void decreaseScraps() {
		this.scraps--;
	}

	public void increaseLikes() {
		this.likes++;
	}

	public void decreaseLikes() {
		this.likes--;
	}

	public void increaseComments(){
		this.comments++;
	}

	public void decreaseComments(){
		this.comments--;
	}

	public void updateStatus(PostStatus postStatus) {
		this.postStatus = postStatus;
	}
}
