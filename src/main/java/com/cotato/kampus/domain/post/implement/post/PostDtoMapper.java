package com.cotato.kampus.domain.post.implement.post;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.HomeBoardAndPostPreview;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.domain.PostDto;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDtoMapper {

	private final PostRepository postRepository;
	private final PostPhotoFinder postPhotoFinder;
	private final PostScrapFinder postScrapFinder;
	private final BoardFinder boardFinder;

	public List<HomeBoardAndPostPreview> mapToHomeBoardAndPostPreviewsByBoardDtos(List<Board> boards) {
		// 각 boardId에 대해 가장 최근의 PostDto 조회
		return boards.stream()
			.map(board -> {
				PostDto latestPost = postRepository.findTopByBoardIdOrderByCreatedTimeDesc(board.getId())
					.map(PostDto::from)
					.orElse(null);
				return HomeBoardAndPostPreview.from(board, latestPost);
			})
			.toList();
	}

	public PostThumbnail toPostThumbnail(Post post, Long userId) {
		String thumbnail = postPhotoFinder.findFirstPhoto(post.getId());
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(userId, post.getId());
		return PostThumbnail.from(post, thumbnail, isScrapped);
	}

	public Slice<PostThumbnail> toPostThumbnails(Slice<Post> posts, Long userId) {
		return posts.map(post -> toPostThumbnail(post, userId));
	}

	public PostThumbnailWithBoardName toPostThumbnailWithBoardName(Post post, Long userId) {
		String thumbnail = postPhotoFinder.findFirstPhoto(post.getId());
		Board board = boardFinder.findBoard(post.getBoardId());
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(userId, post.getId());
		return PostThumbnailWithBoardName.from(post, board, thumbnail, isScrapped);
	}

	public Slice<PostThumbnailWithBoardName> toPostThumbnailsWithBoardName(Slice<Post> posts, Long userId) {
		return posts.map(post -> toPostThumbnailWithBoardName(post, userId));
	}
}