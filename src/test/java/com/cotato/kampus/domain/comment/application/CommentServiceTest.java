package com.cotato.kampus.domain.comment.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.TestPostHelper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.helper.TestUserHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserValidator userValidator;
    @Mock
    private CommentValidator commentValidator;
    @Mock
    private ApiUserResolver apiUserResolver;
    @Mock
    private PostFinder postFinder;
    @Mock
    private AnonymousNumberAllocator anonymousNumberAllocator;
    @Mock
    private CommentAppender commentAppender;
    @Mock
    private PostUpdater postUpdater;

    @Test
    @DisplayName("새로운 사용자 댓글 작성: 두 카운터 모두 증가시키는 메서드 호출")
    void createComment_forNewUser() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        Long commenterId = 2L;
        Long universityId = 1L;
        UserDto commenter = TestUserHelper.createUserDto(commenterId, universityId, UserRole.VERIFIED);

        Post post = new TestPostHelper().withUserId(authorId).createNormalPost(); // 글쓴이 ID : 1

        given(apiUserResolver.getCurrentUserDto()).willReturn(commenter);
        given(postFinder.find(postId)).willReturn(post);
        given(anonymousNumberAllocator.allocateAnonymousNumber(any(Post.class), any(UserDto.class), anyBoolean()))
            .willReturn(new AnonymousAllocationResult(1, true)); // needsIncrement = true

        // when
        commentService.createComment(postId, "첫 댓글입니다", null, null);

        // then
        verify(postUpdater).increaseCommentAndAnonymousCount(any(Post.class));
        verify(postUpdater, never()).increaseCommentCount(any(Post.class));
    }

    @Test
    @DisplayName("글쓴이 댓글 작성: 댓글 카운터만 증가시키는 메서드 호출")
    void createComment_forAuthor() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        Long universityId = 1L;
        UserDto author = TestUserHelper.createUserDto(authorId, universityId, UserRole.VERIFIED);
        Post post = new TestPostHelper().withUserId(author.id()).createNormalPost();

        given(apiUserResolver.getCurrentUserDto()).willReturn(author);
        given(postFinder.find(postId)).willReturn(post);
        given(anonymousNumberAllocator.allocateAnonymousNumber(any(Post.class), any(UserDto.class), anyBoolean()))
            .willReturn(new AnonymousAllocationResult(null, false)); // needsIncrement = false

        // when
        commentService.createComment(postId, "제 글입니다", null, null);

        // then
        verify(postUpdater, never()).increaseCommentAndAnonymousCount(any(Post.class));
        verify(postUpdater).increaseCommentCount(any(Post.class));
    }

    @Test
    @DisplayName("기존 사용자 댓글 작성: 댓글 카운터만 증가시키는 메서드 호출")
    void createComment_forExistingUser() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        Long commenterId = 2L;
        Long universityId = 1L;
        UserDto commenter = TestUserHelper.createUserDto(commenterId, universityId, UserRole.VERIFIED);
        Post post = new TestPostHelper().withUserId(authorId).createNormalPost();

        given(apiUserResolver.getCurrentUserDto()).willReturn(commenter);
        given(postFinder.find(postId)).willReturn(post);
        given(anonymousNumberAllocator.allocateAnonymousNumber(any(Post.class), any(UserDto.class), anyBoolean()))
            .willReturn(new AnonymousAllocationResult(1, false)); // needsIncrement = false

        // when
        commentService.createComment(postId, "또 왔어요", null, null);

        // then
        verify(postUpdater, never()).increaseCommentAndAnonymousCount(any(Post.class));
        verify(postUpdater).increaseCommentCount(any(Post.class));
    }
}
