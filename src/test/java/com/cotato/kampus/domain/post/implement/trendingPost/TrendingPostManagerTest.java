package com.cotato.kampus.domain.post.implement.trendingPost;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.post.implement.port.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.TrendingPost;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrendingPostManagerTest {

	@InjectMocks
	private TrendingPostManager trendingPostManager;

	@Mock
	private TrendingPostFinder trendingPostFinder;
	@Mock
	private TrendingPostRepository trendingPostRepository;

	private final Long postId = 1L;

	@Test
	@DisplayName("좋아요가 5개 이상이고 트렌딩에 없으면 추가한다")
	void handleLikeCountChange_ShouldAddToTrending_WhenLikeCountIsAboveThresholdAndNotTrending() {
		// given
		int likeCount = 5;
		given(trendingPostFinder.existsByPostId(postId)).willReturn(false);

		// when
		trendingPostManager.handleLikeCountChange(postId, likeCount);

		// then
		verify(trendingPostRepository, times(1)).save(any(TrendingPost.class));
		verify(trendingPostRepository, never()).deleteByPostId(postId);
	}

	@Test
	@DisplayName("좋아요가 5개 이상이지만 이미 트렌딩에 있으면 아무것도 하지 않는다")
	void handleLikeCountChange_ShouldNotAddToTrending_WhenLikeCountIsAboveThresholdButAlreadyTrending() {
		// given
		int likeCount = 7;
		given(trendingPostFinder.existsByPostId(postId)).willReturn(true);

		// when
		trendingPostManager.handleLikeCountChange(postId, likeCount);

		// then
		verify(trendingPostRepository, never()).save(any(TrendingPost.class));
		verify(trendingPostRepository, never()).deleteByPostId(postId);
	}

	@Test
	@DisplayName("좋아요가 5개 미만이고 트렌딩에 있으면 제거한다")
	void handleLikeCountChange_ShouldRemoveFromTrending_WhenLikeCountIsBelowThresholdAndTrending() {
		// given
		int likeCount = 4;
		given(trendingPostFinder.existsByPostId(postId)).willReturn(true);

		// when
		trendingPostManager.handleLikeCountChange(postId, likeCount);

		// then
		verify(trendingPostRepository, times(1)).deleteByPostId(postId);
		verify(trendingPostRepository, never()).save(any(TrendingPost.class));
	}

	@Test
	@DisplayName("좋아요가 5개 미만이고 트렌딩에 없으면 아무것도 하지 않는다")
	void handleLikeCountChange_ShouldNotRemoveFromTrending_WhenLikeCountIsBelowThresholdButNotTrending() {
		// given
		int likeCount = 2;
		given(trendingPostFinder.existsByPostId(postId)).willReturn(false);

		// when
		trendingPostManager.handleLikeCountChange(postId, likeCount);

		// then
		verify(trendingPostRepository, never()).save(any(TrendingPost.class));
		verify(trendingPostRepository, never()).deleteByPostId(postId);
	}

	@Test
	@DisplayName("좋아요가 정확히 5개일 때 트렌딩에 추가한다")
	void handleLikeCountChange_ShouldAddToTrending_WhenLikeCountIsExactlyThreshold() {
		// given
		int likeCount = 5;
		given(trendingPostFinder.existsByPostId(postId)).willReturn(false);

		// when
		trendingPostManager.handleLikeCountChange(postId, likeCount);

		// then
		verify(trendingPostRepository, times(1)).save(any(TrendingPost.class));
		verify(trendingPostRepository, never()).deleteByPostId(postId);
	}

}