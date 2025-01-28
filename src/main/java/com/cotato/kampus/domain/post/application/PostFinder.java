package com.cotato.kampus.domain.post.application;

import java.util.List;

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
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;
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
	public static final String SORT_PROPERTY = "createdTime";
	private final ApiUserResolver apiUserResolver;
	private final BoardFinder boardFinder;
	private final PostScrapRepository postScrapRepository;
	private final PostDraftPhotoRepository postDraftPhotoRepository;

	public Post getPost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
	}


	public Slice<PostWithPhotos> findPosts(Long boardId, int page) {
		// 1. Post 리스트를 Slice로 조회
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<Post> posts = postRepository.findAllByBoardIdOrderByCreatedTimeDesc(
			boardId,
			customPageRequest.of(SORT_PROPERTY)
		);

		// 2. Post -> PostWithPhotos 매핑
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

	public Slice<MyPostWithPhoto> findUserPosts(int page){

		Long userId = apiUserResolver.getUserId();

		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<Post> posts = postRepository.findAllByUserId(userId, customPageRequest.of(SORT_PROPERTY));

		return posts.map(post -> {
			String boardName = boardFinder.findBoard(post.getBoardId()).getBoardName();

			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeAsc(post.getId())
				.orElse(null);

			return MyPostWithPhoto.from(post, boardName, postPhoto);
		});
	}

	public Slice<MyPostWithPhoto> findUserScrapedPosts(int page){

		Long userId = apiUserResolver.getUserId();
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);

		// 스크랩된 포스트만 조회
		Slice<PostScrap> postScraps = postScrapRepository.findAllByUserId(userId, customPageRequest.of(SORT_PROPERTY));

		// 스크랩된 포스트에 해당하는 Post를 찾아서 반환
		return postScraps.map(postScrap -> {
			Post post = getPost(postScrap.getPostId());

			String boardName = boardFinder.findBoard(post.getBoardId()).getBoardName();

			PostPhoto postPhoto = postPhotoRepository.findFirstByPostIdOrderByCreatedTimeAsc(post.getId())
				.orElse(null);

			return MyPostWithPhoto.from(post, boardName, postPhoto);
		});
	}

	public Slice<PostDraftWithPhoto> findPostDrafts(Long boardId, Long userId, int page){

		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<PostDraft> postDrafts = postDraftRepository.findAllByBoardIdAndUserIdOrderByCreatedTimeDesc(
			boardId,
			userId,
			customPageRequest.of(SORT_PROPERTY)
		);

		return postDrafts.map(postDraft -> {
			PostDraftPhoto postDraftPhoto = postDraftPhotoRepository.findFirstByPostDraftIdOrderByCreatedTimeDesc(postDraft.getId())
				.orElse(null);
			return PostDraftWithPhoto.from(postDraft, postDraftPhoto);
		});
	}

	public PostDraft findPostDraft(Long postDraftId) {
		return postDraftRepository.findById(postDraftId)
			.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
	}

	public List<PostDraft> findPostDrafts(List<Long> postDraftIds){
		List<PostDraft> drafts = postDraftRepository.findAllById(postDraftIds);

		if (drafts.size() != postDraftIds.size()) {
			throw new AppException(ErrorCode.POST_NOT_FOUND);
		}

		return drafts;
	}
}
