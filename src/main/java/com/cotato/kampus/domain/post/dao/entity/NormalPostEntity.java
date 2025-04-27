package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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

	@Override
	public Post toDomain() {
		return NormalPost.builder()
			.id(id)
			.boardId(boardId)
			.userId(userId)
			.title(title)
			.content(content)
			.postStatus(postStatus)
			.anonymity(anonymity)
			.likeCount(likeCount)
			.commentCount(commentCount)
			.scrapCount(scrapCount)
			.anonymousCount(anonymousCount)
			.createdTime(getCreatedTime())
			.lastModifiedTime(getLastModifiedTime())
			.build();
	}

	public static NormalPostEntity fromDomain(NormalPost normalPost) {
		NormalPostEntity entity = new NormalPostEntity();
		entity.id = normalPost.getId();
		entity.boardId = normalPost.getBoardId();
		entity.userId = normalPost.getUserId();
		entity.title = normalPost.getTitle();
		entity.content = normalPost.getContent();
		entity.postStatus = normalPost.getPostStatus();
		entity.anonymity = normalPost.getAnonymity();
		entity.likeCount = normalPost.getLikeCount();
		entity.commentCount = normalPost.getCommentCount();
		entity.scrapCount = normalPost.getScrapCount();
		entity.anonymousCount = normalPost.getAnonymousCount();
		return entity;
	}

}
