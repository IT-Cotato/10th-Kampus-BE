package com.cotato.kampus.domain.comment.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dao.CommentLikeRepository;
import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentDto;
import com.cotato.kampus.domain.comment.enums.CommentStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentMapper {

	private final AnonymousNumberAllocator anonymousNumberAllocator;
	private final CommentLikeRepository commentLikeRepository;
	private final CommentFinder commentFinder;

	public List<CommentDetail> buildCommentHierarchy(List<CommentDto> allCommentDtos, Long userId){
		Map<Long, CommentDetail> commentMap = new HashMap<>();

		// 모든 댓글을 CommentDetail 객체로 변환하여 Map에 저장
		for (CommentDto dto : allCommentDtos) {
			String targetAuthor = (dto.targetId() != null) ?
				anonymousNumberAllocator.resolveAuthorName(commentFinder.findCommentDto(dto.targetId())) : null;

			CommentDetail detail = CommentDetail.of(
				dto,
				anonymousNumberAllocator.resolveAuthorName(dto),
				targetAuthor,
				new ArrayList<>(),
				commentLikeRepository.existsByUserIdAndCommentId(userId, dto.commentId())
			);
			commentMap.put(dto.commentId(), detail);
		}

		// 부모-자식 관계 형성
		for (CommentDetail detail : commentMap.values()) {
			if(detail.parentId() != null) {
				CommentDetail parent = commentMap.get(detail.parentId());
				if(parent != null) {
					parent.replies().add(detail);
					parent.replies().sort(Comparator.comparing(CommentDetail::createdTime));
				}
			}
		}

		// 최종 필터링 및 내용 변경
		List<CommentDetail> rootComments = new ArrayList<>();
		for(CommentDetail detail : commentMap.values()) {
			// 최상위 댓글 대상으로 필터링 시작
			if(detail.parentId() == null) {
				boolean isDeleted = detail.commentStatus() != CommentStatus.NORMAL;
				boolean hasReplies = !detail.replies().isEmpty();

				if (isDeleted && hasReplies) {
					// 삭제됐지만 대댓글이 있는 경우: 내용 변경 후 추가
					rootComments.add(detail.withMaskedContent());
				} else if (!isDeleted) {
					rootComments.add(detail);
				}
				// 삭제됐고 대댓글도 없는 경우는 아무것도 하지 않음 (결과에서 제외)
			}
		}

		rootComments.sort(Comparator.comparing(CommentDetail::createdTime));

		return rootComments;
	}

}
