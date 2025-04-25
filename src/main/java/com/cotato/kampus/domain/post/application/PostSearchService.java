package com.cotato.kampus.domain.post.application;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostSearchHistoryList;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryAppender;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryDeleter;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryFinder;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostSearchService {

	private final ApiUserResolver apiUserResolver;
	private final PostDtoMapper postDtoMapper;
	private final PostFinder postFinder;

	private final PostSearchHistoryAppender postSearchHistoryAppender;
	private final PostSearchHistoryFinder postSearchHistoryFinder;
	private final PostSearchHistoryValidator postSearchHistoryValidator;
	private final PostSearchHistoryDeleter postSearchHistoryDeleter;

	public Slice<PostThumbnailWithBoardName> searchAllPosts(String keyword, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);

		// 검색 결과 리턴
		Slice<Post> searchedPosts = postFinder.searchAllPosts(keyword, page);

		return postDtoMapper.toPostThumbnailsWithBoardName(searchedPosts, userId);
	}

	public Slice<PostThumbnail> searchBoardPosts(String keyword, Long boardId, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);

		Slice<Post> searchedPosts = postFinder.searchBoardPosts(keyword, boardId, page);

		return postDtoMapper.toPostThumbnails(searchedPosts, userId);
	}

	public PostSearchHistoryList findSearchKeyword() {
		Long userId = apiUserResolver.getCurrentUserId();
		return postSearchHistoryFinder.findByUserId(userId);
	}

	@Transactional
	public Long deleteSearchKeyword(Long keywordId) {
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryValidator.validateUser(userId, keywordId);
		postSearchHistoryDeleter.deleteHistory(keywordId);
		return keywordId;
	}

	@Transactional
	public void deleteAllSearchKeyword() {
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryDeleter.deleteAllHistory(userId);
	}
}
