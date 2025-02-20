package com.cotato.kampus.domain.post.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.HomeBoardAndPostPreview;
import com.cotato.kampus.domain.post.dao.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.TrendingPostPreview;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDtoMapper {

	private final BoardFinder boardFinder;
	private final PostPhotoRepository postPhotoRepository;

	public List<HomeBoardAndPostPreview> mapToHomeBoardAndPostPreviews(List<PostDto> trendingPosts) {
		// boardId -> BoardDto 매핑
		Map<Long, BoardDto> boardDtoMap = trendingPosts.stream()
			.map(PostDto::boardId)
			.distinct()
			.collect(Collectors.toMap(
				boardId -> boardId,
				boardFinder::findBoardDto
			));

		// PostDto와 BoardDto를 HomeBoardAndPostPreview로 매핑
		return trendingPosts.stream()
			.map(postDto -> HomeBoardAndPostPreview.from(
				boardDtoMap.get(postDto.boardId()), postDto)
			).toList();
	}

	public List<TrendingPostPreview> toTrendingPostPreviews(List<Post> posts) {
		// 게시판 ID -> BoardDto 매핑
		Map<Long, String> boardNameMap = posts.stream()
			.map(Post::getBoardId)
			.distinct()
			.collect(Collectors.toMap(
				boardId -> boardId,
				boardId -> boardFinder.findBoardDto(boardId).boardName()
			));

		// Post -> TrendingPostPreview 변환
		return posts.stream()
			.map(post -> {
				PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTime(post.getId())
					.orElse(null);
				return TrendingPostPreview.from(post, boardNameMap.get(post.getBoardId()), postPhoto);
			})
			.toList();
	}
}