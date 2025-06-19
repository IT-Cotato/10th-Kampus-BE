package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetails(
	Long postId,
	Long boardId,
	String boardName,
	BoardType boardType,
	String title,
	String content,
	int likeCount,
	int scrapCount,
	int commentCount,
	PostStatus postStatus,
	List<PostPhotoInfo> postPhotos,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isLiked,
	boolean isScrapped,
	List<String> categoryNames
) {
	public static PostDetails of(
		Post post,
		Board board,
		List<Category> categories,
		List<PostPhoto> postPhotos,
		boolean isAuthor,
		boolean isLiked,
		boolean isScrapped
	) {
		return new PostDetails(
			post.getId(),
			post.getBoardId(),
			board.getBoardName(),
			board.getBoardType(),
			post.getTitle(),
			post.getContent(),
			post.getLikeCount(),
			post.getScrapCount(),
			post.getCommentCount(),
			post.getPostStatus(),
			toPostPhotoInfos(postPhotos),
			post.getCreatedTime(),
			isAuthor,
			isLiked,
			isScrapped,
			toCategoryNames(categories)
		);
	}

	private static List<PostPhotoInfo> toPostPhotoInfos(List<PostPhoto> postPhotos) {
		return postPhotos.stream()
			.map(PostPhotoInfo::from)
			.toList();
	}

	private static List<String> toCategoryNames(List<Category> categories) {
		return categories.stream()
			.map(Category::getCategoryName)
			.toList();
	}
}