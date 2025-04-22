package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("NORMAL")
@Table(name = "normal_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalPostEntity extends PostEntity {

	@Column(name = "content", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "anonymity", nullable = false)
	private Anonymity anonymity;

	@Override
	public Post toDomain() {
		return NormalPost.builder()
			.id(id)
			.boardId(boardId)
			.userId(userId)
			.title(title)
			.postStatus(postStatus)
			.content(content)
			.anonymity(anonymity)
			.build();
	}

	public static NormalPostEntity fromDomain(NormalPost normalPost) {
		NormalPostEntity entity = new NormalPostEntity();
		entity.id = normalPost.getId();
		entity.boardId = normalPost.getBoardId();
		entity.userId = normalPost.getUserId();
		entity.title = normalPost.getTitle();
		entity.postStatus = normalPost.getPostStatus();
		entity.content = normalPost.getContent();
		entity.anonymity = normalPost.getAnonymity();
		return entity;
	}

}
