package com.cotato.kampus.domain.post.implement.postSrcap;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;

@ExtendWith(MockitoExtension.class)
class PostScrapFinderTest {

	@InjectMocks
	private PostScrapFinder postScrapFinder;

	@Mock
	private PostScrapRepository postScrapRepository;

	@Test
	@DisplayName("사용자가 특정 게시글을 스크랩했다면 true를 반환해야 한다")
	void isPostScrappedByUser_WhenScrapped_ShouldReturnTrue() {
		// given
		Long userId = 1L;
		Long postId = 100L;

		when(postScrapRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

		// when
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(postId, userId);

		// then
		assertThat(isScrapped).isTrue();
		verify(postScrapRepository).existsByPostIdAndUserId(postId, userId);
	}

	@Test
	@DisplayName("사용자가 특정 게시글을 스크랩하지 않았다면 false를 반환해야 한다")
	void isPostScrappedByUser_WhenNotScrapped_ShouldReturnFalse() {
		// given
		Long userId = 1L;
		Long postId = 100L;

		when(postScrapRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);

		// when
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(postId, userId);

		// then
		assertThat(isScrapped).isFalse();
		verify(postScrapRepository).existsByPostIdAndUserId(postId, userId);
	}
}