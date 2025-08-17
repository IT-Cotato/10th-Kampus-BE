package com.cotato.kampus.domain.post.implement.post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.implement.port.PostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostReferenceDto;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFinder {

	private final PostRepository postRepository;
	private static final Integer PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";
	private static final int HOME_POST_PREVIEW_LIMIT = 5;

	public Post find(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
	}

	public Slice<Post> findAllByBoardId(Long boardId, int page, PostSortType sortType) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, sortType.getDirection());
		return postRepository.findAllByBoardIdAndPostStatus(boardId, PostStatus.PUBLISHED, customPageRequest.of(sortType.getProperty()));
	}

	public Slice<Post> findAllByBoardIdAndCategoryId(Long boardId, List<Long> postIds, int page, PostSortType sortType) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, sortType.getDirection());
		return postRepository.findAllByBoardIdAndIdInAndPostStatus(boardId, postIds, PostStatus.PUBLISHED, customPageRequest.of(sortType.getProperty()));
	}

	public Slice<Post> findPublishedByIds(List<Long> postIds, int page){
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return postRepository.findAllByIdInAndPostStatus(postIds, PostStatus.PUBLISHED, customPageRequest.of(SORT_PROPERTY));
	}

	public Slice<Post> findAllTrendingPosts(List<Long> trendingPostIds, Long userUniversityId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return postRepository.findAllAccessiblePostsByIds(trendingPostIds, userUniversityId, customPageRequest.of(SORT_PROPERTY));
	}

	public List<Post> findTopTrendingPosts(List<Long> trendingPostIds, Long userUniversityId) {
		CustomPageRequest customPageRequest = new CustomPageRequest(1, HOME_POST_PREVIEW_LIMIT, Sort.Direction.DESC);
		return postRepository.findTopAccessiblePostsByIds(trendingPostIds, userUniversityId, customPageRequest.of(SORT_PROPERTY))
			.getContent();
	}

	public Map<Long, Post> findLatestPostsByBoardIds(List<Long> boardIds) {
		List<Post> latestPosts = postRepository.findLatestPostPerBoard(boardIds, PostStatus.PUBLISHED.name());
		return latestPosts.stream()
				.collect(Collectors.toMap(Post::getBoardId, Function.identity()));
	}

	public List<Post> findTop5ByBoardId(Long boardId) {
		CustomPageRequest customPageRequest = new CustomPageRequest(1, HOME_POST_PREVIEW_LIMIT, Sort.Direction.DESC);
		return postRepository.findByBoardIdAndPostStatusOrderByCreatedTimeDesc(boardId, PostStatus.PUBLISHED, customPageRequest.of(SORT_PROPERTY))
			.getContent();
	}

	public PostReferenceDto findPostReference(Long postId) {
		return postRepository.findById(postId)
			.map(PostReferenceDto::from)
			.orElse(PostReferenceDto.deleted());
	}

	public Slice<Post> findAllByUserId(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return postRepository.findAllByUserIdAndPostStatus(userId, PostStatus.PUBLISHED, customPageRequest.of(SORT_PROPERTY));
	}

	public Slice<Post> searchAllPosts(String keyword, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return postRepository.searchAll(keyword, customPageRequest.of(SORT_PROPERTY));
	}

	public Slice<Post> searchBoardPosts(String keyword, Long boardId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return postRepository.searchAllByBoardId(keyword, boardId, customPageRequest.of(SORT_PROPERTY));
	}

	public Slice<Post> findCommentedPosts(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		// 한 번의 JOIN 쿼리로 게시글 정보 조회
		return postRepository.findPostsByUserComments(userId, customPageRequest.of(SORT_PROPERTY));
	}

	public Long countByBoardId(Long boardId) {
		return postRepository.countByBoardId(boardId);
	}
}