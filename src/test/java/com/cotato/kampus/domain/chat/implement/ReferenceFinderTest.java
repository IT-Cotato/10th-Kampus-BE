package com.cotato.kampus.domain.chat.implement;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cotato.kampus.common.TestEntityFactory;
import com.cotato.kampus.domain.chat.domain.ChatReference;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;

class ReferenceFinderTest {

	@Mock
	private PostFinder postFinder;
	@Mock
	private ProductFinder productFinder;

	@InjectMocks
	private ReferenceFinder referenceFinder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("ChatType.POST일 때 PostFinder를 사용해 ChatReference를 반환한다.")
	void findReferenceByPostType() {
		Long referenceId = 1L;
		Post post = TestEntityFactory.createPost(referenceId, 2L, "post title", 3L);
		when(postFinder.find(referenceId)).thenReturn(post);

		ChatReference result = referenceFinder.find(referenceId, ChatType.POST);

		assertThat(result.getReferenceId()).isEqualTo(referenceId);
		assertThat(result.getReferenceUserId()).isEqualTo(2L);
		assertThat(result.getTitle()).isEqualTo("post title");
		assertThat(result.getBoardId()).isEqualTo(3L);
	}

	@Test
	@DisplayName("ChatType.PRODUCT일 때 ProductFinder를 사용해 ChatReference를 반환한다.")
	void findReferenceByProductType() {
		Long referenceId = 10L;
		Product product = TestEntityFactory.createProduct(referenceId, 20L, "product title");
		when(productFinder.findById(referenceId)).thenReturn(product);

		ChatReference result = referenceFinder.find(referenceId, ChatType.PRODUCT);

		assertThat(result.getReferenceId()).isEqualTo(referenceId);
		assertThat(result.getReferenceUserId()).isEqualTo(20L);
		assertThat(result.getTitle()).isEqualTo("product title");
		assertThat(result.getBoardId()).isNull();
	}
} 