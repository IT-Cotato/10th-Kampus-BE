package com.cotato.kampus.domain.post.implement.post;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.post.implement.port.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDraftDto;
import com.cotato.kampus.domain.post.domain.PostDto;
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

	public void validatePostDraftOwner(PostDraftDto postDraftDto, Long userId) {
		if (!postDraftDto.userId().equals(userId)) {
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
		}
	}

	public void validateDeleteCardNews(Long postId) {
		PostDto postDto = postFinder.findPost(postId);
		Board board = boardFinder.findBoard(postDto.boardId());

		if (board.getBoardType() != BoardType.CARDNEWS) {
			throw new AppException(ErrorCode.CARD_NEWS_NOT_ALLOWED);
		}
	}

	public void validatePublishable(PostDraftDto postDraftDto) {
		if(postDraftDto.title().isEmpty() || postDraftDto.content().isEmpty())
			throw new AppException(ErrorCode.POST_REQUIRED_FIELD_MISSING);
	}
}
