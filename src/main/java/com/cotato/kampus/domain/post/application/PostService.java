package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.board.BoardValidator;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryValidator;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.comment.application.CommentDeleter;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.post.implement.post.PostAppender;
import com.cotato.kampus.domain.post.implement.post.PostDeleter;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryAppender;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryDeleter;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeDeleter;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeFinder;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoAppender;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoDeleter;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapDeleter;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;
import com.cotato.kampus.domain.post.domain.PostDetails;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostDeleter;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostFinder;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostService {

	// 게시글 핵심 기능
	private final PostAppender postAppender;
	private final PostDeleter postDeleter;
	private final PostFinder postFinder;
	private final PostUpdater postUpdater;

	// 게시글 이미지 관련
	private final PostPhotoAppender postPhotoAppender;
	private final PostPhotoFinder postPhotoFinder;
	private final PostPhotoDeleter postPhotoDeleter;

	// 게시글 카테고리 관련
	private final CategoryFinder categoryFinder;
	private final PostCategoryAppender postCategoryAppender;
	private final PostCategoryDeleter postCategoryDeleter;
	private final PostCategoryFinder postCategoryFinder;

	// 게시판 관련
	private final BoardValidator boardValidator;
	private final BoardFinder boardFinder;
	private final BoardCategoryValidator boardCategoryValidator;

	// 상호작용 관련
	private final PostLikeFinder postLikeFinder;
	private final PostLikeDeleter postLikeDeleter;
	private final PostScrapFinder postScrapFinder;
	private final PostScrapDeleter postScrapDeleter;

	// 트렌딩 게시글 관련
	private final TrendingPostDeleter trendingPostDeleter;
	private final TrendingPostFinder trendingPostFinder;

	// 이미지 업로드 및 검증
	private final S3Uploader s3Uploader;
	private final ImageValidator imageValidator;
	private static final String POST_IMAGE_FOLDER = "post";

	private final ApiUserResolver apiUserResolver;
	private final PostDtoMapper postDtoMapper;
	private final CommentDeleter commentDeleter;

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		List<MultipartFile> images,
		List<String> categoryNames
	) throws ImageException {
		// 1. 유저 및 게시판 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		Board board = boardFinder.findBoard(boardId);
		board.validateActive();
		boardValidator.validatePostCreationAccess(user, board);

		// 카테고리 조회, 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryValidator.validateMatching(categoryIds, boardId);

		// 유효한 이미지 필터링 & S3 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// Post, PostPhoto, PostCategory 추가
		Post post = postAppender.appendNormalPost(user.id(), board.getId(), title, content);
		postPhotoAppender.appendAll(post.getId(), imageUrls);
		postCategoryAppender.appendAll(post.getId(), categoryIds);

		return post.getId();
	}

	public Slice<PostThumbnail> findPosts(
		Long boardId,
		int page,
		PostSortType sortType,
		String categoryName) {
		// 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		// Board 조회, 유효성 검사
		Board board = boardFinder.findBoard(boardId);
		board.validateActive();
		boardValidator.validateUniversityAccess(user, board);

		Slice<Post> posts;

		if (categoryName != null && !categoryName.isEmpty()) {
			// 카테고리 필터링
			Category category = categoryFinder.find(categoryName);
			boardCategoryValidator.validateMatching(category.getId(), boardId);
			List<Long> postIds = postCategoryFinder.findAllPostIdsByCategoryId(category.getId());
			posts = postFinder.findAllByBoardIdAndCategoryId(boardId, postIds, page, sortType);
		} else {
			posts = postFinder.findAllByBoardId(boardId, page, sortType);
		}

		return postDtoMapper.toPostThumbnails(posts, user.id());
	}

	@Transactional
	public Long deletePost(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// Post 조회 & 작성자 검증
		Post post = postFinder.find(postId);
		post.validateAuthor(userId);

		// S3 이미지 삭제
		List<String> imageUrls = postPhotoFinder.findPostPhotos(postId).stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
		s3Uploader.deleteFiles(imageUrls);

		// PostPhoto, PostCategory, PostLike, PostScrap 삭제
		postPhotoDeleter.deletePostPhotos(postId);
		postCategoryDeleter.deleteAllByPostId(postId);
		postLikeDeleter.deleteAllByPostId(postId);
		postScrapDeleter.deleteAllByPostId(postId);

		// Comment, CommentLike 삭제
		commentDeleter.deleteAllByPostId(postId);

		// TrendingPost 삭제
		trendingPostDeleter.deleteByPostId(postId);

		// Post 삭제
		postDeleter.delete(post);

		return postId;
	}

	public Slice<PostThumbnailWithBoardName> findTrendingPosts(int page) {
		// 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		// Trending 게시글 조회 (타 대학 게시글 제외)
		List<Long> trendingPostIds = trendingPostFinder.findAllPostIds();
		Slice<Post> trendingPosts = postFinder.findAllTrendingPosts(trendingPostIds, user.universityId(), page);

		return postDtoMapper.toPostThumbnailsWithBoardName(trendingPosts, user.id());
	}

	public PostDetails findPostDetail(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// Post 조회
		Post post = postFinder.find(postId);
		Board board = boardFinder.findBoard(post.getBoardId());

		// PostPhoto 조회
		List<PostPhoto> postPhotos = postPhotoFinder.findPostPhotos(postId);

		List<Long> categoryIds = postCategoryFinder.findCategoryIdsByPostId(post.getId());
		List<Category> categories = categoryFinder.findAllByIds(categoryIds);

		boolean isAuthor = post.getUserId().equals(userId);
		boolean isLiked = postLikeFinder.hasUserLikedPost(userId, postId);
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(userId, postId);
		return PostDetails.of(post, board, categories, postPhotos, isAuthor, isLiked, isScrapped);
	}

	@Transactional
	public void updatePost(
		Long postId,
		String title,
		String content,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validateAuthor(userId);

		// 2. 카테고리 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryValidator.validateMatching(categoryIds, post.getBoardId());

		// 3. 이미지 처리
		// 3.1 새 이미지 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 3.2 기존 이미지 삭제
		List<String> deleteImageUrls = postPhotoFinder.findPostPhotos(postId).stream()
			.map(PostPhoto::getPhotoUrl)
			.toList();
		s3Uploader.deleteFiles(deleteImageUrls);

		// 4. 연관 데이터 갱신
		// 4.1 기존 데이터 삭제
		postPhotoDeleter.deletePostPhotos(postId);
		postCategoryDeleter.deleteAllByPostId(postId);

		// 4.2 새 데이터 추가
		postCategoryAppender.appendAll(postId, categoryIds);
		postPhotoAppender.appendAll(post.getId(), imageUrls);

		// 5. 게시글 내용 업데이트
		postUpdater.update(post, title, content);
	}
}