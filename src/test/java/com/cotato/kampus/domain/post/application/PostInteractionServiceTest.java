package com.cotato.kampus.domain.post.application;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.domain.TestPostHelper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.post.PostValidator;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeAppender;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeDeleter;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeValidator;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapAppender;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapDeleter;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapFinder;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostManager;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostInteractionServiceTest {

    @InjectMocks
    private PostInteractionService postInteractionService;

    @Mock
    private ApiUserResolver apiUserResolver;
    @Mock
    private PostFinder postFinder;
    @Mock
    private PostUpdater postUpdater;
    @Mock
    private PostValidator postValidator;
    @Mock
    private PostLikeAppender postLikeAppender;
    @Mock
    private PostLikeFinder postLikeFinder;
    @Mock
    private PostLikeDeleter postLikeDeleter;
    @Mock
    private PostLikeValidator postLikeValidator;
    @Mock
    private TrendingPostManager trendingPostManager;
    @Mock
    private PostScrapAppender postScrapAppender;
    @Mock
    private PostScrapFinder postScrapFinder;
    @Mock
    private PostScrapDeleter postScrapDeleter;

    private final Long postId = 1L;
    private final Long userId = 1L;

    @Test
    @DisplayName("게시글 스크랩 성공")
    void scrapPost_success() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        doNothing().when(postValidator).validateDuplicatedScrap(postId, userId);
        when(postUpdater.increaseScrapCount(post)).thenReturn(post);
        doNothing().when(postScrapAppender).append(postId, userId);

        // when
        postInteractionService.scrapPost(postId);

        // then
        verify(postValidator).validateDuplicatedScrap(postId, userId);
        verify(postUpdater).increaseScrapCount(post);
        verify(postScrapAppender).append(postId, userId);
    }

    @Test
    @DisplayName("게시글 스크랩 실패 - 이미 스크랩한 경우")
    void scrapPost_fail_alreadyScrapped() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        doThrow(new AppException(ErrorCode.POST_SCRAP_DUPLICATED))
                .when(postValidator).validateDuplicatedScrap(postId, userId);

        // when & then
        AppException exception = assertThrows(AppException.class,
                () -> postInteractionService.scrapPost(postId));
        assertEquals(ErrorCode.POST_SCRAP_DUPLICATED, exception.getErrorCode());

        verify(postUpdater, never()).increaseScrapCount(any(Post.class));
        verify(postScrapAppender, never()).append(anyLong(), anyLong());
    }

    @Test
    @DisplayName("게시글 스크랩 취소 성공")
    void unscrapPost_success() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        PostScrap postScrap = new PostScrap(1L, post.getId(), userId);
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        given(postScrapFinder.find(userId, postId)).willReturn(postScrap);
        when(postUpdater.decreaseScrapCount(post)).thenReturn(post);
        doNothing().when(postScrapDeleter).delete(postScrap);

        // when
        postInteractionService.unscrapPost(postId);

        // then
        verify(postScrapFinder).find(userId, postId);
        verify(postUpdater).decreaseScrapCount(post);
        verify(postScrapDeleter).delete(postScrap);
    }

    @Test
    @DisplayName("게시글 스크랩 취소 실패 - 스크랩하지 않은 경우")
    void unscrapPost_fail_notScrapped() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        given(postScrapFinder.find(userId, postId))
                .willThrow(new AppException(ErrorCode.POST_SCRAP_NOT_EXIST));

        // when & then
        AppException exception = assertThrows(AppException.class,
                () -> postInteractionService.unscrapPost(postId));
        assertEquals(ErrorCode.POST_SCRAP_NOT_EXIST, exception.getErrorCode());

        verify(postUpdater, never()).decreaseScrapCount(any(Post.class));
        verify(postScrapDeleter, never()).delete(any(PostScrap.class));
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    void likePost_success() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        Post updatedPost = new TestPostHelper().createPostWithLikeCount(1);
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        doNothing().when(postLikeValidator).validateDuplicateLike(postId, userId);
        when(postLikeAppender.append(postId, userId)).thenReturn(PostLike.builder().id(1L).postId(postId).userId(userId).build());
        when(postUpdater.increaseLikeCount(post)).thenReturn(updatedPost);
        doNothing().when(trendingPostManager).handleLikeCountChange(postId, updatedPost.getLikeCount());

        // when
        postInteractionService.likePost(postId);

        // then
        verify(postLikeValidator).validateDuplicateLike(postId, userId);
        verify(postLikeAppender).append(postId, userId);
        verify(postUpdater).increaseLikeCount(post);
        verify(trendingPostManager).handleLikeCountChange(postId, updatedPost.getLikeCount());
    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 이미 좋아요한 경우")
    void likePost_fail_alreadyLiked() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        doThrow(new AppException(ErrorCode.POST_LIKE_DUPLICATED))
                .when(postLikeValidator).validateDuplicateLike(postId, userId);

        // when & then
        AppException exception = assertThrows(AppException.class,
                () -> postInteractionService.likePost(postId));
        assertEquals(ErrorCode.POST_LIKE_DUPLICATED, exception.getErrorCode());

        verify(postLikeAppender, never()).append(anyLong(), anyLong());
        verify(postUpdater, never()).increaseLikeCount(any(Post.class));
        verify(trendingPostManager, never()).handleLikeCountChange(anyLong(), anyInt());
    }

    @Test
    @DisplayName("게시글 좋아요 취소 성공")
    void unlikePost_success() {
        // given
        Post post = new TestPostHelper().createPostWithLikeCount(5);
        Post updatedPost = new TestPostHelper().createPostWithLikeCount(4);
        PostLike postLike = PostLike.builder().id(1L).postId(postId).userId(userId).build();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        given(postLikeFinder.findPostLikeByPostIdAndUserId(postId, userId)).willReturn(postLike);
        doNothing().when(postLikeDeleter).delete(postLike);
        when(postUpdater.decreaseLikeCount(post)).thenReturn(updatedPost);
        doNothing().when(trendingPostManager).handleLikeCountChange(postId, updatedPost.getLikeCount());

        // when
        postInteractionService.unlikePost(postId);

        // then
        verify(postLikeFinder).findPostLikeByPostIdAndUserId(postId, userId);
        verify(postLikeDeleter).delete(postLike);
        verify(postUpdater).decreaseLikeCount(post);
        verify(trendingPostManager).handleLikeCountChange(postId, updatedPost.getLikeCount());
    }

    @Test
    @DisplayName("게시글 좋아요 취소 실패 - 좋아요하지 않은 경우")
    void unlikePost_fail_notLiked() {
        // given
        Post post = new TestPostHelper().createNormalPost();
        given(apiUserResolver.getCurrentUserId()).willReturn(userId);
        given(postFinder.find(postId)).willReturn(post);
        given(postLikeFinder.findPostLikeByPostIdAndUserId(postId, userId))
                .willThrow(new AppException(ErrorCode.POST_LIKE_NOT_FOUND));

        // when & then
        AppException exception = assertThrows(AppException.class,
                () -> postInteractionService.unlikePost(postId));
        assertEquals(ErrorCode.POST_LIKE_NOT_FOUND, exception.getErrorCode());

        verify(postLikeDeleter, never()).delete(any(PostLike.class));
        verify(postUpdater, never()).decreaseLikeCount(any(Post.class));
        verify(trendingPostManager, never()).handleLikeCountChange(anyLong(), anyInt());
    }
}
