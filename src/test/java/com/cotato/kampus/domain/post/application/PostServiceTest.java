package com.cotato.kampus.domain.post.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.TestBoardHelper;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostDetails;
import com.cotato.kampus.domain.post.domain.TestPostHelper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryFinder;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private PostFinder postFinder;

	@Mock
	private BoardFinder boardFinder;

	@Mock
	private PostPhotoFinder postPhotoFinder;

	@Mock
	private PostCategoryFinder postCategoryFinder;

	@Mock
	private CategoryFinder categoryFinder;

	@Mock
	private PostLikeFinder postLikeFinder;

	@Mock
	private PostScrapFinder postScrapFinder;

	@Test
	@DisplayName("게시글 상세 조회 시 모든 의존성이 올바르게 호출된다")
	void findPostDetail_Success() {
		// given
		Long postId = 1L;
		Long userId = 100L;

		Post post = new TestPostHelper()
			.withId(postId)
			.withUserId(userId)
			.createNormalPost();

		Board board = TestBoardHelper.createNormalBoard(
			post.getBoardId(),
			false,
			BoardStatus.ACTIVE);

		List<Long> categoryIds = List.of();

		given(apiUserResolver.getCurrentUserId()).willReturn(userId);
		given(postFinder.find(postId)).willReturn(post);
		given(boardFinder.findBoard(post.getBoardId())).willReturn(board);
		given(postPhotoFinder.findPostPhotos(postId)).willReturn(List.of());
		given(postCategoryFinder.findCategoryIdsByPostId(postId)).willReturn(List.of());
		given(categoryFinder.findAllByIds(categoryIds)).willReturn(List.of());
		given(postScrapFinder.isPostScrappedByUser(postId, userId)).willReturn(true);
		given(postLikeFinder.hasUserLikedPost(userId, postId)).willReturn(false);

		// when
		PostDetails result = postService.findPostDetail(postId);

		// then
		assertThat(result).isNotNull();
		then(apiUserResolver).should().getCurrentUserId();
		then(postFinder).should().find(postId);
		then(boardFinder).should().findBoard(post.getBoardId());
		then(postPhotoFinder).should().findPostPhotos(postId);
		then(postCategoryFinder).should().findCategoryIdsByPostId(postId);
		then(categoryFinder).should().findAllByIds(categoryIds);
		then(postLikeFinder).should().hasUserLikedPost(userId, postId);
		then(postScrapFinder).should().isPostScrappedByUser(postId, userId);
	}
}