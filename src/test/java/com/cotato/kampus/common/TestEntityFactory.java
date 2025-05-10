package com.cotato.kampus.common;

import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.product.domain.Product;
import static org.mockito.Mockito.*;

public class TestEntityFactory {

	public static Post createPost(Long id, Long userId, String title, Long boardId) {
		return NormalPost.builder()
			.id(id)
			.userId(userId)
			.title(title)
			.boardId(boardId)
			.content("test content")
			.postStatus(PostStatus.PUBLISHED)
			.anonymity(Anonymity.IDENTIFIED)
			.likeCount(0)
			.commentCount(0)
			.scrapCount(0)
			.anonymousCount(0)
			.build();
	}

	public static Product createProduct(Long id, Long userId, String title) {
		Product realProduct = Product.create(userId, title, 1000, "test description");
		Product spyProduct = spy(realProduct);
		when(spyProduct.getId()).thenReturn(id);
		return spyProduct;
	}
} 