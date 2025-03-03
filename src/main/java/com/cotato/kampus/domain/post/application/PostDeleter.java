package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftRepository;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.post.dao.TrendingPostRepository;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDraft;
import com.cotato.kampus.domain.post.domain.TrendingPost;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDeleter {
	private final PostRepository postRepository;
	private final PostFinder postFinder;
	private final PostDraftRepository postDraftRepository;
	private final TrendingPostRepository trendingPostRepository;
	private static final int TRENDING_LIKE_THRESHOLD = 3;


	@Transactional
	public void delete(Long postId) {
		Post post = postFinder.getPost(postId);
		postRepository.delete(post);
	}

	@Transactional
	public void deleteDraftAll(List<Long> postDraftIds) {
		// 임시 저장글 조회
		List<PostDraft> drafts = postFinder.findPostDrafts(postDraftIds);

		// 일괄 삭제
		postDraftRepository.deleteAll(drafts);
	}

	@Transactional
	public void deletePostsByBoardIds(List<Long> boardIds) {
		postRepository.deleteAllByBoardIdIn(boardIds);
	}

	@Transactional
	public void deleteTrendingPost(Long postId) {
		Post post = postFinder.getPost(postId);

		if (post.getLikes() == TRENDING_LIKE_THRESHOLD) {
			trendingPostRepository.deleteByPostId(postId);
		}
	}
}
