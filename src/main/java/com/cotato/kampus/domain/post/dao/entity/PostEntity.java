package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.CardNewsPost;
import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostType;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "board_id", nullable = false)
	private Long boardId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_status", nullable = false)
	private PostStatus postStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_type", nullable = false)
	private PostType postType;

	public static PostEntity fromDomain(Post post) {
		PostEntity result = new PostEntity();
		result.id = post.getId();
		result.boardId = post.getBoardId();
		result.userId = post.getUserId();
		result.title = post.getTitle();
		result.content = post.getContent();
		result.anonymity = post.getAnonymity();
		result.postStatus = post.getPostStatus();
		result.postType = post.getPostType();
		return result;
	}

	public Post toDomain() {
		switch (postType) {
			case NORMAL:
				return NormalPost.builder()
					.id(this.id)
					.boardId(this.boardId)
					.userId(this.userId)
					.title(this.title)
					.content(this.content)
					.anonymity(this.anonymity)
					.postStatus(this.postStatus)
					.postType(this.postType)
					.build();

			case CARDNEWS:
				return CardNewsPost.builder()
					.id(this.id)
					.boardId(this.boardId)
					.userId(this.userId)
					.title(this.title)
					.anonymity(this.anonymity)
					.postStatus(this.postStatus)
					.postType(this.postType)
					.build();

			default:
				throw new AppException(ErrorCode.INVALID_POST_TYPE);
		}
	}
}
