package com.cotato.kampus.domain.comment.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.comment.enums.ReportStatus;
import com.cotato.kampus.domain.common.enums.Anonymity;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

	@InjectMocks
	private CommentMapper commentMapper;

	@Mock
	private AnonymousNumberAllocator anonymousNumberAllocator;

	@Mock
	private CommentLikeRepository commentLikeRepository;


	@Test
	@DisplayName("현재 유저가 작성한 댓글의 isAuthor는 true여야 한다")
	void buildCommentHierarchy_userIsAuthor_shouldReturnTrue() {
		// given
		Long currentUserId = 1L;
		Long commentId = 10L;
		
		CommentDto commentDto = new CommentDto(
			commentId,
			currentUserId, // 현재 유저가 작성한 댓글
			1L,
			"Test content",
			0L,
			ReportStatus.NORMAL,
			CommentStatus.NORMAL,
			Anonymity.ANONYMOUS,
			0L,
			1,
			null,
			null,
			LocalDateTime.now()
		);

		given(anonymousNumberAllocator.resolveAuthorName(commentDto)).willReturn("Anonymous1");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, anyList()))
			.willReturn(emptyList()); // 좋아요하지 않음

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(Arrays.asList(commentDto), currentUserId);

		// then
		assertThat(result).hasSize(1);
		CommentDetail commentDetail = result.get(0);
		assertThat(commentDetail.isAuthor()).isTrue();
		assertThat(commentDetail.commentId()).isEqualTo(commentId);
	}

	@Test
	@DisplayName("현재 유저가 작성하지 않은 댓글의 isAuthor는 false여야 한다")
	void buildCommentHierarchy_userIsNotAuthor_shouldReturnFalse() {
		// given
		Long currentUserId = 1L;
		Long otherUserId = 2L;
		Long commentId = 10L;
		
		CommentDto commentDto = new CommentDto(
			commentId,
			otherUserId, // 다른 유저가 작성한 댓글
			1L,
			"Test content",
			0L,
			ReportStatus.NORMAL,
			CommentStatus.NORMAL,
			Anonymity.ANONYMOUS,
			0L,
			1,
			null,
			null,
			LocalDateTime.now()
		);

		given(anonymousNumberAllocator.resolveAuthorName(commentDto)).willReturn("Anonymous1");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, anyList()))
			.willReturn(emptyList()); // 좋아요하지 않음

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(Arrays.asList(commentDto), currentUserId);

		// then
		assertThat(result).hasSize(1);
		CommentDetail commentDetail = result.get(0);
		assertThat(commentDetail.isAuthor()).isFalse();
		assertThat(commentDetail.commentId()).isEqualTo(commentId);
	}

	@Test
	@DisplayName("대댓글도 isAuthor 필드가 올바르게 설정되어야 한다")
	void buildCommentHierarchy_withReplies_shouldSetIsAuthorCorrectly() {
		// given
		Long currentUserId = 1L;
		Long otherUserId = 2L;
		
		// 부모 댓글 (다른 유저 작성)
		CommentDto parentComment = new CommentDto(
			10L,
			otherUserId,
			1L,
			"Parent comment",
			0L,
			ReportStatus.NORMAL,
			CommentStatus.NORMAL,
			Anonymity.ANONYMOUS,
			0L,
			1,
			null,
			null,
			LocalDateTime.now()
		);

		// 대댓글 (현재 유저 작성)
		CommentDto replyComment = new CommentDto(
			11L,
			currentUserId,
			1L,
			"Reply comment",
			0L,
			ReportStatus.NORMAL,
			CommentStatus.NORMAL,
			Anonymity.ANONYMOUS,
			0L,
			2,
			10L, // 부모 댓글 ID
			null,
			LocalDateTime.now().plusMinutes(1)
		);

		given(anonymousNumberAllocator.resolveAuthorName(parentComment)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(replyComment)).willReturn("Anonymous2");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, anyList()))
			.willReturn(emptyList()); // 좋아요하지 않음

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(parentComment, replyComment), currentUserId);

		// then
		assertThat(result).hasSize(1);
		
		CommentDetail parentDetail = result.get(0);
		assertThat(parentDetail.isAuthor()).isFalse(); // 다른 유저가 작성
		assertThat(parentDetail.replies()).hasSize(1);
		
		CommentDetail replyDetail = parentDetail.replies().get(0);
		assertThat(replyDetail.isAuthor()).isTrue(); // 현재 유저가 작성
	}

	// ========== 댓글 상태 처리 테스트 ==========

	@Test
	@DisplayName("삭제된 댓글이지만 대댓글이 있으면 내용을 마스킹하여 포함해야 한다")
	void buildCommentHierarchy_deletedCommentWithReplies_shouldMaskContent() {
		// given
		Long currentUserId = 1L;
		
		// 삭제된 부모 댓글
		CommentDto deletedParent = new CommentDto(
			10L, 2L, 1L, "Original content", 0L,
			ReportStatus.NORMAL, CommentStatus.DELETED_BY_USER, // 삭제됨
			Anonymity.ANONYMOUS, 0L, 1, null, null,
			LocalDateTime.now()
		);
		
		// 대댓글
		CommentDto reply = new CommentDto(
			11L, currentUserId, 1L, "Reply to deleted", 0L,
			ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, null,
			LocalDateTime.now().plusMinutes(1)
		);

		given(anonymousNumberAllocator.resolveAuthorName(deletedParent)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(reply)).willReturn("Anonymous2");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(10L, 11L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(deletedParent, reply), currentUserId);

		// then
		assertThat(result).hasSize(1);
		CommentDetail parentDetail = result.get(0);
		assertThat(parentDetail.content()).isEqualTo("This comment was deleted."); // 마스킹됨
		assertThat(parentDetail.replies()).hasSize(1);
	}

	@Test
	@DisplayName("삭제된 댓글이고 대댓글도 없으면 결과에서 완전히 제외해야 한다")
	void buildCommentHierarchy_deletedCommentWithoutReplies_shouldExclude() {
		// given
		Long currentUserId = 1L;
		
		// 삭제된 댓글 (대댓글 없음)
		CommentDto deletedComment = new CommentDto(
			10L, 2L, 1L, "To be deleted", 0L,
			ReportStatus.NORMAL, CommentStatus.DELETED_BY_USER, // 삭제됨
			Anonymity.ANONYMOUS, 0L, 1, null, null,
			LocalDateTime.now()
		);
		
		// 정상 댓글
		CommentDto normalComment = new CommentDto(
			11L, currentUserId, 1L, "Normal comment", 0L,
			ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, null, null,
			LocalDateTime.now().plusMinutes(1)
		);

		given(anonymousNumberAllocator.resolveAuthorName(deletedComment)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(normalComment)).willReturn("Anonymous2");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(10L, 11L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(deletedComment, normalComment), currentUserId);

		// then
		assertThat(result).hasSize(1); // 삭제된 댓글은 제외됨
		assertThat(result.get(0).commentId()).isEqualTo(11L); // 정상 댓글만 포함
	}

	// ========== 좋아요 기능 테스트 ==========

	@Test
	@DisplayName("좋아요한 댓글의 isLiked는 true여야 한다")
	void buildCommentHierarchy_likedComment_shouldReturnTrue() {
		// given
		Long currentUserId = 1L;
		Long commentId = 10L;
		
		CommentDto commentDto = new CommentDto(
			commentId, 2L, 1L, "Liked comment", 5L,
			ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null,
			LocalDateTime.now()
		);

		given(anonymousNumberAllocator.resolveAuthorName(commentDto)).willReturn("Anonymous1");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(commentId)))
			.willReturn(Arrays.asList(commentId)); // 좋아요함

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(Arrays.asList(commentDto), currentUserId);

		// then
		assertThat(result.get(0).isLiked()).isTrue();
		assertThat(result.get(0).likes()).isEqualTo(5L);
	}

	@Test
	@DisplayName("좋아요하지 않은 댓글의 isLiked는 false여야 한다")
	void buildCommentHierarchy_notLikedComment_shouldReturnFalse() {
		// given
		Long currentUserId = 1L;
		Long commentId = 10L;
		
		CommentDto commentDto = new CommentDto(
			commentId, 2L, 1L, "Not liked comment", 3L,
			ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null,
			LocalDateTime.now()
		);

		given(anonymousNumberAllocator.resolveAuthorName(commentDto)).willReturn("Anonymous1");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(commentId)))
			.willReturn(emptyList()); // 좋아요하지 않음

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(Arrays.asList(commentDto), currentUserId);

		// then
		assertThat(result.get(0).isLiked()).isFalse();
		assertThat(result.get(0).likes()).isEqualTo(3L);
	}

	// ========== 정렬 기능 테스트 ==========

	@Test
	@DisplayName("최상위 댓글들이 시간순으로 정렬되어야 한다")
	void buildCommentHierarchy_rootComments_shouldBeSortedByTime() {
		// given
		Long currentUserId = 1L;
		LocalDateTime baseTime = LocalDateTime.now();
		
		// 시간 순서대로 생성하지 않음 (의도적으로)
		CommentDto comment3 = new CommentDto(
			30L, 2L, 1L, "Third comment", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, baseTime.plusMinutes(3)
		);
		
		CommentDto comment1 = new CommentDto(
			10L, 2L, 1L, "First comment", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, baseTime.plusMinutes(1)
		);
		
		CommentDto comment2 = new CommentDto(
			20L, 2L, 1L, "Second comment", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, baseTime.plusMinutes(2)
		);

		given(anonymousNumberAllocator.resolveAuthorName(comment1)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(comment2)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(comment3)).willReturn("Anonymous1");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(30L, 10L, 20L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(comment3, comment1, comment2), currentUserId);

		// then
		assertThat(result).hasSize(3);
		assertThat(result.get(0).commentId()).isEqualTo(10L); // 첫 번째
		assertThat(result.get(1).commentId()).isEqualTo(20L); // 두 번째
		assertThat(result.get(2).commentId()).isEqualTo(30L); // 세 번째
	}

	@Test
	@DisplayName("대댓글들이 시간순으로 정렬되어야 한다")
	void buildCommentHierarchy_replies_shouldBeSortedByTime() {
		// given
		Long currentUserId = 1L;
		LocalDateTime baseTime = LocalDateTime.now();
		
		CommentDto parent = new CommentDto(
			10L, 2L, 1L, "Parent comment", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, baseTime
		);
		
		// 대댓글들 (시간 순서 섞임)
		CommentDto reply3 = new CommentDto(
			13L, currentUserId, 1L, "Third reply", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, null, baseTime.plusMinutes(3)
		);
		
		CommentDto reply1 = new CommentDto(
			11L, currentUserId, 1L, "First reply", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, null, baseTime.plusMinutes(1)
		);
		
		CommentDto reply2 = new CommentDto(
			12L, currentUserId, 1L, "Second reply", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, null, baseTime.plusMinutes(2)
		);

		given(anonymousNumberAllocator.resolveAuthorName(parent)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(reply1)).willReturn("Anonymous2");
		given(anonymousNumberAllocator.resolveAuthorName(reply2)).willReturn("Anonymous2");
		given(anonymousNumberAllocator.resolveAuthorName(reply3)).willReturn("Anonymous2");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(10L, 13L, 11L, 12L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(parent, reply3, reply1, reply2), currentUserId);

		// then
		assertThat(result).hasSize(1);
		CommentDetail parentDetail = result.get(0);
		assertThat(parentDetail.replies()).hasSize(3);
		
		// 대댓글들이 시간순으로 정렬되었는지 확인
		assertThat(parentDetail.replies().get(0).commentId()).isEqualTo(11L); // 첫 번째
		assertThat(parentDetail.replies().get(1).commentId()).isEqualTo(12L); // 두 번째
		assertThat(parentDetail.replies().get(2).commentId()).isEqualTo(13L); // 세 번째
	}

	// ========== targetId/parentId 관계 검증 테스트 ==========

	@Test
	@DisplayName("유효한 targetId 관계면 targetAuthor가 설정되어야 한다")
	void buildCommentHierarchy_validTargetRelation_shouldSetTargetAuthor() {
		// given
		Long currentUserId = 1L;
		
		CommentDto parent = new CommentDto(
			10L, 2L, 1L, "Parent comment", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, LocalDateTime.now()
		);
		
		// targetId가 parentId와 같은 경우 (유효)
		CommentDto reply = new CommentDto(
			11L, currentUserId, 1L, "Reply to parent", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, 10L, // parentId = targetId = 10L
			LocalDateTime.now().plusMinutes(1)
		);

		given(anonymousNumberAllocator.resolveAuthorName(parent)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(reply)).willReturn("Anonymous2");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(10L, 11L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(parent, reply), currentUserId);

		// then
		CommentDetail replyDetail = result.get(0).replies().get(0);
		assertThat(replyDetail.targetAuthor()).isEqualTo("Anonymous1"); // targetAuthor 설정됨
	}

	// ========== Edge Case 테스트 ==========

	@Test
	@DisplayName("빈 리스트를 입력하면 빈 결과를 반환해야 한다")
	void buildCommentHierarchy_emptyInput_shouldReturnEmpty() {
		// given
		Long currentUserId = 1L;

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(emptyList(), currentUserId);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("대댓글의 대댓글은 허용되지 않아야 한다")
	void buildCommentHierarchy_nestedReplies_shouldBeRejected() {
		// given
		Long currentUserId = 1L;
		
		CommentDto parent = new CommentDto(
			10L, 2L, 1L, "Parent", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 1, null, null, LocalDateTime.now()
		);
		
		CommentDto reply = new CommentDto(
			11L, 3L, 1L, "Reply", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 2, 10L, null, LocalDateTime.now().plusMinutes(1)
		);
		
		// 대댓글의 대댓글 시도 (거부되어야 함)
		CommentDto nestedReply = new CommentDto(
			12L, currentUserId, 1L, "Nested reply", 0L, ReportStatus.NORMAL, CommentStatus.NORMAL,
			Anonymity.ANONYMOUS, 0L, 3, 11L, null, // reply를 부모로 설정
			LocalDateTime.now().plusMinutes(2)
		);

		given(anonymousNumberAllocator.resolveAuthorName(parent)).willReturn("Anonymous1");
		given(anonymousNumberAllocator.resolveAuthorName(reply)).willReturn("Anonymous2");
		given(anonymousNumberAllocator.resolveAuthorName(nestedReply)).willReturn("Anonymous3");
		given(commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(currentUserId, Arrays.asList(10L, 11L, 12L)))
			.willReturn(emptyList());

		// when
		List<CommentDetail> result = commentMapper.buildCommentHierarchy(
			Arrays.asList(parent, reply, nestedReply), currentUserId);

		// then
		assertThat(result).hasSize(1);
		CommentDetail parentDetail = result.get(0);
		assertThat(parentDetail.replies()).hasSize(1); // reply만 포함
		assertThat(parentDetail.replies().get(0).replies()).isEmpty(); // nested reply는 거부됨
	}
}