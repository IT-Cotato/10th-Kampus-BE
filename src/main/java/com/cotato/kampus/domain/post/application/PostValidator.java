package com.cotato.kampus.domain.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class PostValidator {

	private final PostFinder postFinder;
	private final PostScrapRepository postScrapRepository;
	private final BoardFinder boardFinder;

	public void validatePostOwner(Long postId, Long userId) {
		// Post 조회
		Post post = postFinder.getPost(postId);

		if (!post.getUserId().equals(userId)) {
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
		}
	}

	public void validateDuplicatedScrap(Long postId, Long userId) {
		// 중복 스크랩 예외처리
		if (postScrapRepository.existsByUserIdAndPostId(userId, postId)) {
			throw new AppException(ErrorCode.POST_SCRAP_DUPLICATED);
		}
	}

	public void validateDraftPostDelete(Long draftPostId, Long userId) {
		// 임시저장 게시글 조회
		PostDraft postDraft = postFinder.findPostDraft(draftPostId);

		// 작성자인지 검증
		validatePostDraftOwner(postDraft, userId);
	}

	public void validatePostDraftOwner(PostDraft postDraft, Long userId) {
		if (!postDraft.getUserId().equals(userId)) {
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
		}
	}

	public void validateDeleteCardNews(Long postId) {
		PostDto postDto = postFinder.findPost(postId);
		BoardDto boardDto = boardFinder.findBoardDto(postDto.boardId());

		if (boardDto.boardType() != BoardType.CARDNEWS) {
			throw new AppException(ErrorCode.CARD_NEWS_NOT_ALLOWED);
		}
	}
}
