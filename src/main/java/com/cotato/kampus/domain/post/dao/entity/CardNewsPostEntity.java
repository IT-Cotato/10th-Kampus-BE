package com.cotato.kampus.domain.post.dao.entity;

import com.cotato.kampus.domain.post.domain.CardNewsPost;
import com.cotato.kampus.domain.post.domain.Post;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CARDNEWS")
@Table(name = "cardnews_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardNewsPostEntity extends PostEntity {

	@Override
	public Post toDomain() {
		return CardNewsPost.fromEntity(id, boardId, userId, title, content, postStatus, anonymity,
			likeCount, commentCount, scrapCount, anonymousCount,
			getCreatedTime(), getLastModifiedTime());
	}

	public static CardNewsPostEntity fromDomain(CardNewsPost cardNewsPost) {
		CardNewsPostEntity entity = new CardNewsPostEntity();
		entity.id = cardNewsPost.getId();
		entity.boardId = cardNewsPost.getBoardId();
		entity.userId = cardNewsPost.getUserId();
		entity.title = cardNewsPost.getTitle();
		entity.content = cardNewsPost.getContent();
		entity.postStatus = cardNewsPost.getPostStatus();
		entity.anonymity = cardNewsPost.getAnonymity();
		entity.likeCount = cardNewsPost.getLikeCount();
		entity.commentCount = cardNewsPost.getCommentCount();
		entity.scrapCount = cardNewsPost.getScrapCount();
		entity.anonymousCount = cardNewsPost.getAnonymousCount();
		return entity;
	}
}
