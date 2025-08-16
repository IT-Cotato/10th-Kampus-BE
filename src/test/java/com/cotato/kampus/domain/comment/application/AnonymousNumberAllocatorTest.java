package com.cotato.kampus.domain.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.TestPostHelper;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.helper.TestUserHelper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnonymousNumberAllocatorTest {

    @InjectMocks
    private AnonymousNumberAllocator anonymousNumberAllocator;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private Comment mockComment;

    @Test
    @DisplayName("작성자일 경우 익명번호 null과 증가 필요 없음(false) 반환")
    void allocateAnonymousNumber_forAuthor() {
        // given
        Long userId = 1L;
        Long universityId = 1L;
        UserDto authorDto = TestUserHelper.createUserDto(userId, universityId, UserRole.VERIFIED);
        Post mockPost = new TestPostHelper()
            .withUserId(authorDto.id())
            .createNormalPost();
        boolean isAuthor = true;

        // when
        AnonymousAllocationResult result = anonymousNumberAllocator.allocateAnonymousNumber(mockPost, authorDto, isAuthor);

        // then
        assertThat(result.anonymousNumber()).isNull();
        assertThat(result.needsIncrement()).isFalse();
    }

    @Test
    @DisplayName("새로운 사용자일 경우 다음 번호와 함께 증가 필요(true) 반환")
    void allocateAnonymousNumber_forNewUser() {
        // given
        Long authorId = 1L;
        Long commenterId = 2L;
        Long universityId = 1L;
        UserDto commenterDto = TestUserHelper.createUserDto(commenterId, universityId, UserRole.VERIFIED);
        Post mockPost = new TestPostHelper()
            .withUserId(authorId)
            .withAnonymousCount(2) // 현재 익명 카운트: 2
            .createNormalPost();
        boolean isAuthor = false;

        given(commentRepository.findFirstByPostIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        AnonymousAllocationResult result = anonymousNumberAllocator.allocateAnonymousNumber(mockPost, commenterDto, isAuthor);

        // then
        assertThat(result.anonymousNumber()).isEqualTo(3); // 2 + 1
        assertThat(result.needsIncrement()).isTrue();
    }

    @Test
    @DisplayName("기존 사용자일 경우 기존 번호와 증가 불필요(false) 반환")
    void allocateAnonymousNumber_forExistingUser() {
        // given
        Long authorId = 1L;
        Long commenterId = 2L;
        Long universityId = 1L;
        UserDto commenterDto = TestUserHelper.createUserDto(commenterId, universityId, UserRole.VERIFIED);
        Post mockPost = new TestPostHelper()
            .withUserId(authorId)
            .withAnonymousCount(1) // 현재 익명 카운트: 1
            .createNormalPost();
        boolean isAuthor = false;

        given(commentRepository.findFirstByPostIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(mockComment));
        given(mockComment.getAnonymousNumber()).willReturn(1);

        // when
        AnonymousAllocationResult result = anonymousNumberAllocator.allocateAnonymousNumber(mockPost, commenterDto, isAuthor);

        // then
        assertThat(result.anonymousNumber()).isEqualTo(1);
        assertThat(result.needsIncrement()).isFalse();
    }
}
