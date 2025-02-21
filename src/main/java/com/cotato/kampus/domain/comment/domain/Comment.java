package com.cotato.kampus.domain.comment.domain;

import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.comment.enums.ReportStatus;
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
	@Column(name = "report_status", nullable = false)
	private ReportStatus reportStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "comment_status", nullable = false)
	private CommentStatus commentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Column(name = "anonymous_number")
	private Long anonymousNumber;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "target_id")
	private Long targetId;

	@Column(name = "likes", nullable = false)
	private Long likes = 0L;

	@Column(name = "reports", nullable = false)
	private Long reports = 0L;

	@Builder
	public Comment(Long userId, Long postId, String content,
		Long likes, ReportStatus reportStatus, CommentStatus commentStatus,
		Anonymity anonymity, Long reports, Long anonymousNumber, Long parentId, Long targetId) {
		this.userId = userId;
		this.postId = postId;
		this.content = content;
		this.likes = likes;
		this.reportStatus = reportStatus;
		this.commentStatus = commentStatus;
		this.anonymity = anonymity;
		this.reports = reports;
		this.anonymousNumber = anonymousNumber;
		this.parentId = parentId;
		this.targetId = targetId;
	}

	public void setCommentStatus(CommentStatus commentStatus) {
		this.commentStatus = commentStatus;
	}

	public void increaseLikes() {
		this.likes++;
	}

	public void decreaseLikes() {
		this.likes--;
	}
}
