package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.TemporaryNormalPost;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "temp_normal_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryNormalPostEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "temp_normal_post_id")
	protected Long id;

	@Column(name = "board_id", nullable = false)
	protected Long boardId;

	@Column(name = "user_id", nullable = false)
	protected Long userId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity")
	private Anonymity anonymity;

	public TemporaryNormalPost toDomain() {
		return TemporaryNormalPost.builder()
			.id(id)
			.boardId(boardId)
			.userId(userId)
			.title(title)
			.content(content)
			.anonymity(anonymity)
			.createdTime(getCreatedTime())
			.lastModifiedTime(getLastModifiedTime())
			.build();
	}

	public static TemporaryNormalPostEntity fromDomain(TemporaryNormalPost temporaryNormalPost) {
		TemporaryNormalPostEntity entity = new TemporaryNormalPostEntity();
		entity.id = temporaryNormalPost.getId();
		entity.boardId = temporaryNormalPost.getBoardId();
		entity.userId = temporaryNormalPost.getUserId();
		entity.title = temporaryNormalPost.getTitle();
		entity.content = temporaryNormalPost.getContent();
		entity.anonymity = temporaryNormalPost.getAnonymity();
		return entity;
	}
}
