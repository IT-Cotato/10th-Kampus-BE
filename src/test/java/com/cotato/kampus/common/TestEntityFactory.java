package com.cotato.kampus.common;

import com.cotato.kampus.domain.post.domain.NormalPost;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.product.domain.Product;
import static org.mockito.Mockito.*;

public class TestEntityFactory {

	public static Post createPost(Long id, Long userId, String title, Long boardId) {
		Post realPost = NormalPost.create(boardId, userId, title, "test content", PostStatus.PUBLISHED, Anonymity.IDENTIFIED);
		Post spyPost = spy(realPost);
		when(spyPost.getId()).thenReturn(id);
		return spyPost;
	}

	public static Product createProduct(Long id, Long userId, String title) {
		Product realProduct = Product.create(userId, title, 1000, "test description");
		Product spyProduct = spy(realProduct);
		when(spyProduct.getId()).thenReturn(id);
		return spyProduct;
	}
} 