package com.cotato.kampus.domain.comment.application;

import static java.util.stream.Collectors.*;

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

	public List<CommentDetail> buildCommentHierarchy(List<CommentDto> allCommentDtos, Long userId){
		if (allCommentDtos.isEmpty()) {
			return new ArrayList<>();
		}

		// 1. 필요한 데이터 준비
		Map<Long, CommentDto> commentDtoMap = allCommentDtos.stream()
			.collect(toMap(CommentDto::commentId, dto -> dto));
		Map<Long, Boolean> likedCommentsMap = getLikedCommentsMap(userId, 
			allCommentDtos.stream().map(CommentDto::commentId).toList());

		// 2. 댓글을 CommentDetail로 변환
		Map<Long, CommentDetail> commentMap = convertToCommentDetails(allCommentDtos, commentDtoMap, likedCommentsMap, userId);

		// 3. 부모-자식 관계 형성 및 정렬
		buildParentChildRelationships(commentMap);

		// 4. 최상위 댓글 필터링 및 반환
		return filterAndSortRootComments(commentMap);
	}

	/**
	 * CommentDto를 CommentDetail로 변환하여 Map에 저장
	 */
	private Map<Long, CommentDetail> convertToCommentDetails(List<CommentDto> allCommentDtos, 
			Map<Long, CommentDto> commentDtoMap, Map<Long, Boolean> likedCommentsMap, Long userId) {
		Map<Long, CommentDetail> commentMap = new HashMap<>();
		
		for (CommentDto dto : allCommentDtos) {
			String targetAuthor = resolveTargetAuthor(dto, commentDtoMap);
			
			CommentDetail detail = CommentDetail.of(
				dto,
				anonymousNumberAllocator.resolveAuthorName(dto),
				targetAuthor,
				new ArrayList<>(),
				likedCommentsMap.getOrDefault(dto.commentId(), false),
				userId.equals(dto.userId())
			);
			commentMap.put(dto.commentId(), detail);
		}
		return commentMap;
	}
	
	/**
	 * targetId에 해당하는 작성자 이름 확인
	 */
	private String resolveTargetAuthor(CommentDto dto, Map<Long, CommentDto> commentDtoMap) {
		if (dto.targetId() == null) {
			return null;
		}
		
		CommentDto targetComment = commentDtoMap.get(dto.targetId());
		if (isValidTargetRelation(dto, targetComment)) {
			return anonymousNumberAllocator.resolveAuthorName(targetComment);
		}
		return null;
	}

	/**
	 * 부모-자식 관계 형성 및 대댓글 정렬
	 */
	private void buildParentChildRelationships(Map<Long, CommentDetail> commentMap) {
		// 부모-자식 관계 형성
		for (CommentDetail detail : commentMap.values()) {
			if(detail.parentId() != null) {
				CommentDetail parent = commentMap.get(detail.parentId());
				if(isValidParentChildRelation(detail, parent)) {
					parent.replies().add(detail);
				}
			}
		}
		
		// 대댓글 정렬 (replies가 있는 댓글만 대상)
		commentMap.values().stream()
			.filter(detail -> !detail.replies().isEmpty())
			.forEach(detail -> detail.replies().sort(Comparator.comparing(CommentDetail::createdTime)));
	}

	/**
	 * 최상위 댓글 필터링 및 정렬하여 반환
	 */
	private List<CommentDetail> filterAndSortRootComments(Map<Long, CommentDetail> commentMap) {
		List<CommentDetail> rootComments = new ArrayList<>();
		
		for(CommentDetail detail : commentMap.values()) {
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

	/**
	 * 좋아요 정보를 배치로 조회하여 N+1 문제 해결
	 */
	private Map<Long, Boolean> getLikedCommentsMap(Long userId, List<Long> commentIds) {
		if (commentIds.isEmpty()) {
			return new HashMap<>();
		}
		
		// 좋아요한 댓글 ID들을 배치로 조회
		List<Long> likedCommentIds = commentLikeRepository.findCommentIdsByUserIdAndCommentIdIn(userId, commentIds);
		
		// 결과를 Map으로 변환
		Map<Long, Boolean> result = new HashMap<>();
		for (Long commentId : commentIds) {
			result.put(commentId, likedCommentIds.contains(commentId));
		}
		return result;
	}

	/**
	 * targetId와 parentId 관계가 유효한지 검증
	 * - targetId가 있으면 같은 parentId를 가져야 함 (동일한 댓글 스레드 내에서만 답글 가능)
	 * - 또는 targetId가 parentId와 같아야 함 (최상위 댓글에 대한 직접 답글)
	 */
	private boolean isValidTargetRelation(CommentDto current, CommentDto target) {
		if (current.parentId() == null) {
			return false; // 최상위 댓글은 targetId를 가질 수 없음
		}
		
		// targetId가 parentId와 같거나 (최상위 댓글에 대한 답글)
		// target도 같은 parentId를 가져야 함 (동일 스레드 내 답글)
		return current.targetId().equals(current.parentId()) || 
			   (target.parentId() != null && target.parentId().equals(current.parentId()));
	}

	/**
	 * 부모-자식 관계가 유효한지 검증
	 * - 순환 참조 방지
	 * - 자기 자신을 부모로 설정하는 것 방지
	 */
	private boolean isValidParentChildRelation(CommentDetail child, CommentDetail parent) {
		// 자기 자신을 부모로 설정 불가 또는 대댓글은 최상위 댓글만 부모로 가질 수 있음
		return !child.commentId().equals(parent.commentId()) && parent.parentId() == null;
	}

}
