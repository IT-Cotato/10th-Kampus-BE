package com.cotato.kampus.domain.comment.domain;

import com.cotato.kampus.domain.comment.CommentStatus;
import com.cotato.kampus.domain.model.enums.Anonymity;
import com.cotato.kampus.domain.model.domain.BaseTimeEntity;

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
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "content", columnDefinition = "text")
	private String content;

	@Column(name = "likes")
	private Long likes;

	@Enumerated(EnumType.STRING)
	@Column(name = "comment_status")
	private CommentStatus commentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity")
	private Anonymity anonymity;

	@Column(name = "reports")
	private Long reports;

	@Builder
	public Comment(Long userId, Long postId, String content,
		Long likes, CommentStatus commentStatus,
		Anonymity anonymity, Long reports) {
		this.userId = userId;
		this.postId = postId;
		this.content = content;
		this.likes = likes;
		this.commentStatus = commentStatus;
		this.anonymity = anonymity;
		this.reports = reports;
	}
}
