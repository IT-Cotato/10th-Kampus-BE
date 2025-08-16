package com.cotato.kampus.domain.post.application;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.domain.TestPostHelper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.post.PostValidator;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapAppender;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapDeleter;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;
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
}
