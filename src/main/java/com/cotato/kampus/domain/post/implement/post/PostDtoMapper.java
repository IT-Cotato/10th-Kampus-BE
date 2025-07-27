package com.cotato.kampus.domain.post.implement.post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.response.AdminCardNewsThumbnail;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.HomePostThumbnail;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDtoMapper {

	private final PostPhotoFinder postPhotoFinder;
	private final PostScrapFinder postScrapFinder;
	private final BoardFinder boardFinder;

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

	public AdminCardNewsThumbnail toAdminCardNewsThumbnail(Post post) {
		String thumbnail = postPhotoFinder.findFirstPhoto(post.getId());
		return AdminCardNewsThumbnail.from(post, thumbnail);
	}

	public Slice<AdminCardNewsThumbnail> toAdminCardNewsThumbnails(Slice<Post> posts){
		return posts.map(this::toAdminCardNewsThumbnail);
	}

	public HomePostThumbnail toHomePostThumbnail(Post post) {
		Board board = boardFinder.findBoard(post.getBoardId());
		return HomePostThumbnail.from(board, post);
	}

	public List<HomePostThumbnail> toHomePostThumbnails(List<Post> posts) {
		return posts.stream().map(this::toHomePostThumbnail).toList();
	}

	public List<HomePostThumbnail> toHomePostThumbnails(
		Map<Long, Board> boards,
		Map<Long, Optional<Post>> postsByBoardId
	) {
		return boards.entrySet().stream()
			.map(entry -> {
				Long boardId = entry.getKey();
				Board board = entry.getValue();
				Post post = postsByBoardId.getOrDefault(boardId, Optional.empty()).orElse(null);
				return HomePostThumbnail.from(board, post);
			})
			.toList();
	}
}