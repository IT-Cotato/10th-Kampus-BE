package com.cotato.kampus.domain.post.dao.factory;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.post.dao.entity.CardNewsPostEntity;
import com.cotato.kampus.domain.post.dao.entity.NormalPostEntity;
import com.cotato.kampus.domain.post.dao.entity.PostEntity;
import com.cotato.kampus.domain.post.domain.CardNewsPost;
import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

/**
 * 도메인 객체와 엔티티 간 변환을 담당하는 팩토리 클래스
 */
@Component
public class PostFactory {

	public PostEntity createEntity(Post post) {
		if (post instanceof NormalPost) {
			return  NormalPostEntity.fromDomain((NormalPost)post);
		} else if (post instanceof CardNewsPost) {
			return CardNewsPostEntity.fromDomain((CardNewsPost)post);
		} else {
			throw new AppException(ErrorCode.POST_TYPE_MISMATCH);
		}
	}
}
