package com.cotato.kampus.domain.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.comment.dao.CommentRepository;
import com.cotato.kampus.domain.comment.domain.Comment;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.enums.CommentStatus;
import com.cotato.kampus.domain.comment.enums.ReportStatus;
import com.cotato.kampus.domain.common.enums.Anonymity;

@ExtendWith(MockitoExtension.class)
class CommentFinderTest {

	@InjectMocks
	private CommentFinder commentFinder;

	@Mock
	private CommentRepository commentRepository;

	@Test
	@DisplayName("REMOVED 상태 댓글은 조회에서 제외되어야 한다")
	void findAllDtoByPostId_removedComment_shouldExclude() {
		// given
		Long postId = 1L;

		Comment normalComment = Comment.builder()
			.userId(1L)
			.postId(postId)
			.content("Normal comment")
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.NORMAL)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(1)
			.parentId(null)
			.targetId(null)
			.build();

		Comment removedComment = Comment.builder()
			.userId(2L)
			.postId(postId)
			.content("Removed comment")
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.REMOVED)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(2)
			.parentId(null)
			.targetId(null)
			.build();

		Comment maskedComment = Comment.builder()
			.userId(3L)
			.postId(postId)
			.content("Masked comment")
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.MASKED)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(3)
			.parentId(null)
			.targetId(null)
			.build();

		given(commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId))
			.willReturn(Arrays.asList(normalComment, removedComment, maskedComment));

		// when
		List<CommentDto> result = commentFinder.findAllDtoByPostId(postId);

		// then
		assertThat(result).hasSize(2); // REMOVED 댓글은 제외

		List<CommentStatus> resultStatuses = result.stream()
			.map(CommentDto::commentStatus)
			.toList();

		assertThat(resultStatuses).containsExactly(CommentStatus.NORMAL, CommentStatus.MASKED);
		assertThat(resultStatuses).doesNotContain(CommentStatus.REMOVED);
	}

	@Test
	@DisplayName("NORMAL과 MASKED 상태 댓글은 조회에 포함되어야 한다")
	void findAllDtoByPostId_normalAndMaskedComments_shouldInclude() {
		// given
		Long postId = 1L;

		Comment normalComment = Comment.builder()
			.userId(1L)
			.postId(postId)
			.content("Normal comment")
			.likes(3L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.NORMAL)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(1)
			.parentId(null)
			.targetId(null)
			.build();

		Comment maskedComment = Comment.builder()
			.userId(2L)
			.postId(postId)
			.content("Masked comment")
			.likes(7L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.MASKED)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(2)
			.parentId(null)
			.targetId(null)
			.build();

		Comment maskedByAdminComment = Comment.builder()
			.userId(3L)
			.postId(postId)
			.content("Masked by admin comment")
			.likes(2L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.MASKED_BY_ADMIN)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(3)
			.parentId(null)
			.targetId(null)
			.build();

		given(commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId))
			.willReturn(Arrays.asList(normalComment, maskedComment, maskedByAdminComment));

		// when
		List<CommentDto> result = commentFinder.findAllDtoByPostId(postId);

		// then
		assertThat(result).hasSize(3); // 모든 댓글 포함

		List<CommentStatus> resultStatuses = result.stream()
			.map(CommentDto::commentStatus)
			.toList();

		assertThat(resultStatuses).containsExactly(
			CommentStatus.NORMAL,
			CommentStatus.MASKED,
			CommentStatus.MASKED_BY_ADMIN
		);
	}

	@Test
	@DisplayName("모든 댓글이 REMOVED 상태이면 빈 리스트를 반환해야 한다")
	void findAllDtoByPostId_allRemovedComments_shouldReturnEmpty() {
		// given
		Long postId = 1L;

		Comment removedComment1 = Comment.builder()
			.userId(1L)
			.postId(postId)
			.content("Removed comment 1")
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.REMOVED)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(1)
			.parentId(null)
			.targetId(null)
			.build();

		Comment removedComment2 = Comment.builder()
			.userId(2L)
			.postId(postId)
			.content("Removed comment 2")
			.likes(0L)
			.reportStatus(ReportStatus.NORMAL)
			.commentStatus(CommentStatus.REMOVED_BY_ADMIN)
			.anonymity(Anonymity.ANONYMOUS)
			.reports(0L)
			.anonymousNumber(2)
			.parentId(null)
			.targetId(null)
			.build();

		given(commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId))
			.willReturn(Arrays.asList(removedComment1, removedComment2));

		// when
		List<CommentDto> result = commentFinder.findAllDtoByPostId(postId);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("댓글이 없으면 빈 리스트를 반환해야 한다")
	void findAllDtoByPostId_noComments_shouldReturnEmpty() {
		// given
		Long postId = 1L;

		given(commentRepository.findAllByPostIdOrderByCreatedTimeAsc(postId))
			.willReturn(Arrays.asList());

		// when
		List<CommentDto> result = commentFinder.findAllDtoByPostId(postId);

		// then
		assertThat(result).isEmpty();
	}
}