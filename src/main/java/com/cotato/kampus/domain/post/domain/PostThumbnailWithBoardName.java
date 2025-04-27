package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.domain.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 게시글 목록 조회에 사용 (게시판 이름 포함)
 */
public record PostThumbnailWithBoardName(
	Long postId,
	Long boardId,
	String boardName,
	String title,
	String content,
	int likeCount,
	int commentCount,
	int scrapCount,
	String thumbnailUrl,
	boolean isScrapped,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime
) {
	public static PostThumbnailWithBoardName from(Post post, Board board, String photoUrl, boolean isScrapped) {
		return new PostThumbnailWithBoardName(
			post.getId(),
			board.getId(),
			board.getBoardName(),
			post.getTitle(),
			post.getContent(),
			post.getLikeCount(),
			post.getCommentCount(),
			post.getScrapCount(),
			photoUrl,
			isScrapped,
			post.getCreatedTime()
		);
	}
}
