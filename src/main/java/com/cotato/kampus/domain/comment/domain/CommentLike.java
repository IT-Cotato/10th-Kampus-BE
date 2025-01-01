package com.cotato.kampus.domain.comment.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;

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
@Table(name = "comment_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_like_id")
	private Long id;

	@Column(name = "comment_id", nullable = false)
	private Long commentId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "user_id2", nullable = false)
	private Long userId2;

	@Builder
	public CommentLike(Long commentId, Long userId, Long userId2) {
		this.commentId = commentId;
		this.userId = userId;
		this.userId2 = userId2;
	}
}
