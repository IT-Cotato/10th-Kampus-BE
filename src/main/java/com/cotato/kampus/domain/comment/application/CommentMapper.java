package com.cotato.kampus.domain.comment.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.comment.dto.CommentDetail;
import com.cotato.kampus.domain.comment.dto.CommentDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentMapper {

	private final AuthorResolver authorResolver;

	public List<CommentDetail> buildCommentHierarchy(List<CommentDto> commentDtos){
		Map<Long, CommentDetail> commentMap = new HashMap<>();
		List<CommentDetail> rootComments = new ArrayList<>();

		// 모든 댓글을 Map으로 변환
		for (CommentDto commentDto : commentDtos) {
			CommentDetail detail = CommentDetail.of(
				commentDto,
				authorResolver.resolveAuthorName(commentDto),
				new ArrayList<>()
			);
			commentMap.put(commentDto.commentId(), detail);
		}

		// 부모-자식 관계 형성
		for(CommentDto commentDto : commentDtos){
			if(commentDto.parentId() == null){
				rootComments.add(commentMap.get(commentDto.commentId()));
			} else {
				CommentDetail parent = commentMap.get(commentDto.parentId());
				parent.replies().add(commentMap.get(commentDto.commentId()));

			}
		}
		return rootComments;
	}
}
