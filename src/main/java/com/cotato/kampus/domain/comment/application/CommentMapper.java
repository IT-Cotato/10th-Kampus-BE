package com.cotato.kampus.domain.comment.application;

import java.util.ArrayList;
import java.util.List;

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
	private final CommentFinder commentFinder;

	public List<CommentDetail> buildCommentDetails(List<CommentDto> commentDtos){

		List<CommentDetail> commentDetails = new ArrayList<>();

		// 하나씩 돌며 댓글과 대댓글 구분
		for (CommentDto commentDto : commentDtos) {
			List<CommentDto> childComments = commentFinder.findChildComments(commentDto.id());

			// 자식 댓글이 있으면 replies 채운 후 CommentDatails 생성
			if(!childComments.isEmpty()) {
				List<CommentDetail> childCommentDetails = childComments.stream()
					.map(this::mapToChildCommentDetail)
					.toList();

				// 현재 댓글을 부모 댓글로 추가하고 replies를 자식 댓글 리스트로 설정
				commentDetails.add(mapToParentCOmmentDetail(commentDto, childCommentDetails));
			} else if (commentDto.parentId() == null){
				// 대댓글이 없는 댓글이면 리스트로 replies 설정
				commentDetails.add(mapToParentCOmmentDetail(commentDto, List.of()));
			}
		}

		return commentDetails;
	}

	private CommentDetail mapToParentCOmmentDetail(CommentDto commentDto, List<CommentDetail> replies) {
		String authorName = authorResolver.resolveAuthorName(commentDto);
		return CommentDetail.of(
			commentDto,
			authorName,
			replies
		);
	}

	private CommentDetail mapToChildCommentDetail(CommentDto commentDto) {
		String authorName = authorResolver.resolveAuthorName(commentDto);
		return CommentDetail.of(
			commentDto,
			authorName,
			List.of()
		);
	}
}
