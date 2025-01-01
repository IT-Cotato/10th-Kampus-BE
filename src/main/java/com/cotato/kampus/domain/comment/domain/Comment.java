package com.cotato.kampus.domain.comment.domain;

import com.cotato.kampus.domain.comment.enums.RepostStatus;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.common.enums.Anonymity;

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

	@Column(name = "content", nullable = false, columnDefinition = "text")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "comment_status", nullable = false)
	private RepostStatus repostStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Column(name = "likes", nullable = false)
	private Long likes = 0L;

	@Column(name = "reports", nullable = false)
	private Long reports = 0L;

	@Builder
	public Comment(Long userId, Long postId, String content,
		Long likes, RepostStatus repostStatus,
		Anonymity anonymity, Long reports) {
		this.userId = userId;
		this.postId = postId;
		this.content = content;
		this.likes = likes;
		this.repostStatus = repostStatus;
		this.anonymity = anonymity;
		this.reports = reports;
	}
}
