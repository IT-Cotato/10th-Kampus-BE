package com.cotato.kampus.domain.post.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.dao.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.dao.PostDraftRepository;
import com.cotato.kampus.domain.post.dao.PostPhotoRepository;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.dao.PostScrapRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.dto.MyPostWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDraftDto;
import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.post.dto.SearchedPost;
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
	private final PostPhotoRepository postPhotoRepository;
	private final PostDraftRepository postDraftRepository;
	private static final Integer PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";
	private final ApiUserResolver apiUserResolver;
	private final BoardFinder boardFinder;
	private final PostScrapRepository postScrapRepository;
	private final PostDraftPhotoRepository postDraftPhotoRepository;

	public Post getPost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
	}

	public Slice<PostWithPhotos> findPosts(Long boardId, int page, PostSortType sortType) {
		// 1. Post 리스트를 Slice로 조회
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, sortType.getDirection());

		// 2. 정렬 기준에 맞게 조회
		Slice<Post> posts = findPostsBySort(boardId, customPageRequest, sortType);

		// 3. Post -> PostWithPhotos 매핑
		return posts.map(post -> {
			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeDesc(post.getId())
				.orElse(null);
			return PostWithPhotos.from(post, postPhoto);
		});
	}

	// 정렬 기준에 맞는 조회 로직 수행
	private Slice<Post> findPostsBySort(Long boardId, CustomPageRequest pageRequest, PostSortType sortType) {
		return switch (sortType) {
			// 최신순
			case recent -> postRepository.findAllByBoardIdOrderByCreatedTimeDesc(
				boardId,
				pageRequest.of(sortType.getProperty())
			);
			// 오래된순
			case old -> postRepository.findAllByBoardIdOrderByCreatedTimeAsc(
				boardId,
				pageRequest.of(sortType.getProperty())
			);
			// 좋아요순(좋아요가 같을 경우 최신순)
			case likes -> postRepository.findAllByBoardIdOrderByLikesDescCreatedTimeDesc(
				boardId,
				pageRequest.of(sortType.getProperty())
			);
		};
	}

	public Slice<PostWithPhotos> findUserCommentedPosts(List<Long> postIds, int page){

		// CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		PageRequest pageRequest = PageRequest.of(page-1, PAGE_SIZE);
		String orderList = postIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		Slice<Post> posts = postRepository.findPostsByIdsInOrder(postIds, orderList, pageRequest);

		// 3. Post -> PostWithPhotos 매핑
		return posts.map(post -> {
			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeDesc(post.getId())
				.orElse(null);
			return PostWithPhotos.from(post, postPhoto);
		});

	}

	public PostDto findPost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

		return PostDto.from(post);
	}

	public Slice<MyPostWithPhoto> findUserPosts(int page) {

		Long userId = apiUserResolver.getCurrentUserId();

		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<Post> posts = postRepository.findAllByUserId(userId, customPageRequest.of(SORT_PROPERTY));

		return posts.map(post -> {
			String boardName = boardFinder.findBoardDto(post.getBoardId()).boardName();

			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeAsc(post.getId())
				.orElse(null);

			return MyPostWithPhoto.from(post, boardName, postPhoto);
		});
	}

	public Slice<MyPostWithPhoto> findUserScrapedPosts(int page) {

		Long userId = apiUserResolver.getCurrentUserId();
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);

		// 스크랩된 포스트만 조회
		Slice<PostScrap> postScraps = postScrapRepository.findAllByUserId(userId, customPageRequest.of(SORT_PROPERTY));

		// 스크랩된 포스트에 해당하는 Post를 찾아서 반환
		return postScraps.map(postScrap -> {
			Post post = getPost(postScrap.getPostId());

			String boardName = boardFinder.findBoardDto(post.getBoardId()).boardName();

			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeAsc(post.getId())
				.orElse(null);

			return MyPostWithPhoto.from(post, boardName, postPhoto);
		});
	}

	public Slice<PostDraftWithPhoto> findPostDrafts(Long boardId, Long userId, int page) {

		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<PostDraft> postDrafts = postDraftRepository.findAllByBoardIdAndUserIdOrderByCreatedTimeDesc(
			boardId,
			userId,
			customPageRequest.of(SORT_PROPERTY)
		);

		return postDrafts.map(postDraft -> {
			PostDraftPhoto postDraftPhoto = postDraftPhotoRepository.findFirstByPostDraftIdOrderByCreatedTimeDesc(
					postDraft.getId())
				.orElse(null);
			return PostDraftWithPhoto.from(postDraft, postDraftPhoto);
		});
	}

	public PostDraft findPostDraft(Long postDraftId) {
		return postDraftRepository.findById(postDraftId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
	}

	public PostDraftDto findPostDraftDto(Long postDraftId) {
		PostDraft postDraft = findPostDraft(postDraftId);

		return PostDraftDto.from(postDraft);
	}

	public List<PostDraft> findPostDrafts(List<Long> postDraftIds) {
		List<PostDraft> drafts = postDraftRepository.findAllById(postDraftIds);

		if (drafts.size() != postDraftIds.size()) {
			throw new AppException(ErrorCode.POST_NOT_FOUND);
		}

		return drafts;
	}

	public List<Long> getPostDraftIdsByBoardAndUser(Long boardId, Long userId) {
		List<PostDraft> postDrafts = postDraftRepository.findAllByBoardIdAndUserId(boardId, userId);

		return postDrafts.stream()
			.map(PostDraft::getId)
			.toList();
	}

	public Slice<SearchedPost> searchAllPosts(String keyword, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<Post> posts = postRepository.searchAll(keyword, customPageRequest.of(SORT_PROPERTY));
		return mapToSearchedPost(posts);
	}

	public Slice<SearchedPost> searchBoardPosts(String keyword, Long boardId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<Post> posts = postRepository.searchAllByBoardId(keyword, boardId, customPageRequest.of(SORT_PROPERTY));
		return mapToSearchedPost(posts);
	}

	// 게시글 검색 결과에 사진과 게시판을 매핑
	private Slice<SearchedPost> mapToSearchedPost(Slice<Post> posts) {
		return posts.map(post -> {
			String boardName = boardFinder.findBoard(post.getBoardId()).getBoardName();
			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeDesc(post.getId())
				.orElse(null);
			return SearchedPost.of(post, boardName, postPhoto);
		});
	}

}
