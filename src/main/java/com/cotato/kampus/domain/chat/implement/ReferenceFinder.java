package com.cotato.kampus.domain.chat.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceFinder {

	private final PostFinder postFinder;
	private final ProductFinder productFinder;

	public ChatReference find(final Long referenceId, ChatType chatType) {
		if (chatType == ChatType.POST) {
			Post post = postFinder.find(referenceId);
			return ChatReference.builder()
				.referenceId(post.getId())
				.referenceUserId(post.getUserId())
				.title(post.getTitle())
				.boardId(post.getBoardId())
				.build();
		} else {
			// POST가 아닌 경우 모두 Product 채팅 타입으로 처리
			Product product = productFinder.findById(referenceId);
			return ChatReference.builder()
				.referenceId(product.getId())
				.referenceUserId(product.getUserId())
				.title(product.getTitle())
				.boardId(null)
				.build();
		}
	}
}