package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PostEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	protected Long id;

	@Column(name = "board_id", nullable = false)
	protected Long boardId;

	@Column(name = "user_id", nullable = false)
	protected Long userId;

	@Column(name = "title", nullable = false)
	protected String title;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_status", nullable = false)
	protected PostStatus postStatus;

	@Column(name = "like_count", nullable = false, columnDefinition = "int default 0")
	protected int likeCount = 0;

	@Column(name = "comment_count", nullable = false, columnDefinition = "int default 0")
	protected int commentCount = 0;

	@Column(name = "scrap_count", nullable = false, columnDefinition = "int default 0")
	protected int scrapCount = 0;

	@Column(name = "anonymous_count", nullable = false, columnDefinition = "int default 1")
	protected int anonymousCount = 1;

	public abstract Post toDomain();
}
