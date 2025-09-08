package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.post.PostValidator;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeAppender;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeDeleter;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeValidator;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapAppender;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapDeleter;
import com.cotato.kampus.domain.post.implement.postScrap.PostScrapFinder;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostManager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostInteractionService {

	private final ApiUserResolver apiUserResolver;
	private final PostDtoMapper postDtoMapper;

	private final PostFinder postFinder;
	private final PostUpdater postUpdater;
	private final PostValidator postValidator;

	private final PostLikeAppender postLikeAppender;
	private final PostLikeFinder postLikeFinder;
	private final PostLikeDeleter postLikeDeleter;
	private final PostLikeValidator postLikeValidator;

	private final TrendingPostManager trendingPostManager;

	private final PostScrapAppender postScrapAppender;
	private final PostScrapFinder postScrapFinder;
	private final PostScrapDeleter postScrapDeleter;

	@Transactional
	public void likePost(Long postId) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validatePublish();

		// 2. 좋아요 제약 조건 검증(이미 좋아요한 게시글)
		postLikeValidator.validateDuplicateLike(postId, userId);

		// 3. 좋아요 추가
		postLikeAppender.append(postId, userId);

		// 4. post의 likeCount + 1
		Post updatedPost = postUpdater.increaseLikeCount(post);

		// 5. 트렌딩 게시판 관리
		trendingPostManager.handleLikeCountChange(postId, updatedPost.getLikeCount());

	}

	@Transactional
	public void unlikePost(Long postId) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validatePublish();

		// 2. 좋아요 삭제
		PostLike postLike = postLikeFinder.findPostLikeByPostIdAndUserId(postId, userId);
		postLikeDeleter.delete(postLike);

		// 3. post의 likeCount - 1
		Post updatedPost = postUpdater.decreaseLikeCount(post);

		// 4. 트렌딩 게시판 관리
		trendingPostManager.handleLikeCountChange(postId, updatedPost.getLikeCount());

	}

	public Slice<PostThumbnailWithBoardName> findUserPosts(int page) {
		// 1. 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		Slice<Post> posts = postFinder.findAllByUserId(userId, page);

		return postDtoMapper.toPostThumbnailsWithBoardName(posts, userId);
	}

	@Transactional
	public void scrapPost(Long postId) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validatePublish();

		// 2. 스크랩 중복 검증
		postValidator.validateDuplicatedScrap(postId, userId);

		// 3. 게시글 스크랩 수 증가
		postUpdater.increaseScrapCount(post);

		// 4. 스크랩 정보 저장
		postScrapAppender.append(postId, userId);
	}

	@Transactional
	public void unscrapPost(Long postId) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validatePublish();

		// 2. 스크랩 정보 조회
		PostScrap postScrap = postScrapFinder.find(userId, postId);

		// 3. 게시글 스크랩 수 감소
		postUpdater.decreaseScrapCount(post);

		// 4. 스크랩 정보 삭제
		postScrapDeleter.delete(postScrap);
	}

	public Slice<PostThumbnailWithBoardName> findUserScrapedPosts(int page) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();

		// 스크랩된 포스트만 조회
		List<Long> postIds = postScrapFinder.findAllByUserId(userId);

		Slice<Post> posts = postFinder.findPublishedByIds(postIds, page);

		return postDtoMapper.toPostThumbnailsWithBoardName(posts, userId);
	}
}
