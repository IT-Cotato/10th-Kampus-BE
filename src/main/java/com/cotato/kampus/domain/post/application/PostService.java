package com.cotato.kampus.domain.post.application;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.board.BoardValidator;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryResolver;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.comment.application.CommentDeleter;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostLike;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.domain.PostScrap;
import com.cotato.kampus.domain.post.domain.TemporaryPhoto;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPhotoAppender;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPhotoDeleter;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPhotoFinder;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostAppender;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostCategoryDeleter;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostDeleter;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostFinder;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostUpdater;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostCategoryAppender;
import com.cotato.kampus.domain.post.implement.post.PostAppender;
import com.cotato.kampus.domain.post.implement.post.PostDeleter;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
import com.cotato.kampus.domain.post.implement.post.PostValidator;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryAppender;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryDeleter;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeAppender;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeDeleter;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeFinder;
import com.cotato.kampus.domain.post.implement.postLike.PostLikeValidator;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoAppender;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoDeleter;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapAppender;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapDeleter;
import com.cotato.kampus.domain.post.implement.postSrcap.PostScrapFinder;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryAppender;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryDeleter;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryFinder;
import com.cotato.kampus.domain.post.implement.postSearch.PostSearchHistoryValidator;
import com.cotato.kampus.domain.post.implement.trendingPost.TrendingPostAppender;
import com.cotato.kampus.domain.post.domain.PostDetails;
import com.cotato.kampus.domain.post.domain.TempPostDetails;
import com.cotato.kampus.domain.post.domain.TempPostThumbnail;
import com.cotato.kampus.domain.post.domain.PostSearchHistoryList;
import com.cotato.kampus.domain.post.domain.PostThumbnail;
import com.cotato.kampus.domain.post.domain.PostThumbnailWithBoardName;
import com.cotato.kampus.domain.post.enums.PostSortType;
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

	private final PostAppender postAppender;
	private final PostDeleter postDeleter;
	private final PostFinder postFinder;
	private final PostUpdater postUpdater;

	private final PostPhotoAppender postPhotoAppender;
	private final PostPhotoFinder postPhotoFinder;
	private final PostPhotoDeleter postPhotoDeleter;

	private final PostScrapFinder postScrapFinder;
	private final ApiUserResolver apiUserResolver;
	private final S3Uploader s3Uploader;

	private final ImageValidator imageValidator;
	private final PostLikeValidator postLikeValidator;
	private final PostLikeFinder postLikeFinder;
	private final PostValidator postValidator;

	private final PostSearchHistoryAppender postSearchHistoryAppender;
	private final PostSearchHistoryFinder postSearchHistoryFinder;
	private final PostSearchHistoryValidator postSearchHistoryValidator;
	private final PostSearchHistoryDeleter postSearchHistoryDeleter;

	private static final String POST_IMAGE_FOLDER = "post";
	private final BoardValidator boardValidator;
	private final BoardFinder boardFinder;
	private final TrendingPostAppender trendingPostAppender;
	private final BoardCategoryResolver boardCategoryResolver;
	private final PostCategoryAppender postCategoryAppender;
	private final PostCategoryDeleter postCategoryDeleter;
	private final CommentDeleter commentDeleter;
	private final PostCategoryFinder postCategoryFinder;

	private final TemporaryPostAppender temporaryPostAppender;
	private final TemporaryPostFinder temporaryPostFinder;
	private final TemporaryPostDeleter temporaryPostDeleter;
	private final TrendingPostDeleter trendingPostDeleter;
	private final TrendingPostFinder trendingPostFinder;
	private final TemporaryPostUpdater temporaryPostUpdater;

	private final PostLikeAppender postLikeAppender;
	private final PostLikeDeleter postLikeDeleter;

	private final PostScrapAppender postScrapAppender;
	private final PostScrapDeleter postScrapDeleter;
	private final TemporaryPhotoFinder temporaryPhotoFinder;
	private final TemporaryPhotoDeleter temporaryPhotoDeleter;
	private final TemporaryPhotoAppender temporaryPhotoAppender;
	private final CategoryFinder categoryFinder;
	private final TemporaryPostCategoryAppender temporaryPostCategoryAppender;
	private final TemporaryPostCategoryDeleter temporaryPostCategoryDeleter;
	private final PostDtoMapper postDtoMapper;

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
		boardCategoryResolver.validateMatching(categoryIds, boardId);

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

	public Slice<PostThumbnail> findPosts(Long boardId, int page, PostSortType sortType,
		Optional<String> categoryName) {
		// 유저 조회
		UserDto user = apiUserResolver.getCurrentUserDto();

		// Board 조회, 유효성 검사
		Board board = boardFinder.findBoard(boardId);
		board.validateBoardIsActive();
		boardValidator.validateUniversityAccess(user, board);

		Slice<Post> posts;

		if (categoryName.isPresent()) {
			// 카테고리 필터링
			Category category = categoryFinder.find(categoryName.get());
			boardCategoryResolver.validateMatching(category.getId(), boardId);
			List<Long> postIds = postCategoryFinder.findAllPostIdsByCategoryId(category.getId());
			posts = postFinder.findAllByBoardIdAndCategoryId(boardId, postIds, page, sortType);
		} else {
			posts = postFinder.findAllByBoardId(boardId, page, sortType);
		}

		return postDtoMapper.toPostThumbnails(posts, user.id());
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

		// PostPhoto 조회
		List<PostPhoto> postPhotos = postPhotoFinder.findPostPhotos(postId);

		boolean isAuthor = post.getUserId().equals(userId);
		boolean isLiked = postLikeFinder.hasUserLikedPost(userId, postId);
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(userId, postId);
		return PostDetails.of(post, postPhotos, isAuthor, isLiked, isScrapped);
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
		boardCategoryResolver.validateMatching(categoryIds, post.getBoardId());

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

	@Transactional
	public Long createTempPost(
		Long boardId,
		String title,
		String content,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저 및 게시판 조회/검증
		UserDto user = apiUserResolver.getCurrentUserDto();
		Board board = boardFinder.findBoard(boardId);
		board.validateBoardIsActive();
		boardValidator.validatePostCreationAccess(user, board);

		// 2. 카테고리 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryResolver.validateMatching(categoryIds, boardId);

		// 3. 이미지 처리
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 4. 임시 게시글 생성
		TemporaryPost temporaryPost = temporaryPostAppender.append(user.id(), boardId, title, content);

		// 5. 관련 데이터 추가
		temporaryPostCategoryAppender.appendAll(temporaryPost.getId(), categoryIds);
		temporaryPhotoAppender.appendAll(temporaryPost.getId(), imageUrls);

		return temporaryPost.getId();
	}

	@Transactional
	public void deleteSelectedTempPosts(List<Long> tempPostIds) {
		// 1. 유저 및 임시 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		List<TemporaryPost> temporaryPosts = temporaryPostFinder.findAllByIds(tempPostIds);
		temporaryPosts.forEach(temporaryPost -> temporaryPost.validateAuthor(userId));

		// 2 S3이미지 삭제
		List<String> photoUrls = temporaryPhotoFinder.findAllPhotoUrls(tempPostIds);
		s3Uploader.deleteFiles(photoUrls);

		// 3. 관련 데이터 삭제
		temporaryPhotoDeleter.deleteAllByTempPostIds(tempPostIds);
		temporaryPostCategoryDeleter.deleteAllByTemporaryPostIds(tempPostIds);

		// 4. 임시 게시글 삭제
		temporaryPostDeleter.deleteAllByIds(tempPostIds);
	}

	@Transactional
	public void deleteAllTempPost() {
		// 1. 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 임시 게시글 조회
		List<TemporaryPost> temporaryPosts = temporaryPostFinder.findAllByUserId(userId);
		List<Long> tempPostIds = temporaryPosts.stream()
			.map(TemporaryPost::getId)
			.toList();

		// 3. S3 이미지 삭제
		List<String> photoUrls = temporaryPhotoFinder.findAllPhotoUrls(tempPostIds);
		s3Uploader.deleteFiles(photoUrls);

		// 4. 관련 데이터 삭제
		temporaryPhotoDeleter.deleteAllByTempPostIds(tempPostIds);
		temporaryPostCategoryDeleter.deleteAllByTemporaryPostIds(tempPostIds);

		// 5. 임시 게시글 삭제
		temporaryPostDeleter.deleteAllByUser(userId);
	}

	@Transactional
	public Slice<TempPostThumbnail> findPostDrafts(int page) {
		// 1. 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 임시 게시글 조회
		Slice<TemporaryPost> temporaryPosts = temporaryPostFinder.findAllByUserId(userId, page);

		return temporaryPosts.map(tempPost -> {
			String thumbnail = temporaryPhotoFinder.findFirstPhoto(tempPost.getId());
			Board board = boardFinder.findBoard(tempPost.getBoardId());
			return TempPostThumbnail.from(tempPost, board.getBoardName(), thumbnail);
		});
	}

	@Transactional
	public TempPostDetails findTempPostDetail(Long postDraftId) {
		// 1. 유저 및 임시게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		TemporaryPost temporaryPost = temporaryPostFinder.find(postDraftId);
		temporaryPost.validateAuthor(userId);

		List<TemporaryPhoto> tempPhotos = temporaryPhotoFinder.findAllByTempPostId(postDraftId);
		String boardName = boardFinder.findBoard(temporaryPost.getBoardId()).getBoardName();
		return TempPostDetails.of(temporaryPost, boardName, tempPhotos);
	}

	@Transactional
	public Long publishDraftPost(
		Long tempPostId,
		String title,
		String content,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저, 임시 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		TemporaryPost temporaryPost = temporaryPostFinder.find(tempPostId);
		temporaryPost.validateAuthor(userId);

		// 2. 카테고리 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryResolver.validateMatching(categoryIds, temporaryPost.getBoardId());

		// 3. 이미지 처리
		// 3.1 새 이미지 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 3.2 기존 이미지 삭제
		List<String> photoUrls = temporaryPhotoFinder.findAllPhotoUrls(tempPostId);
		s3Uploader.deleteFiles(photoUrls);

		// 4. 임시 게시글 관련 데이터 삭제
		temporaryPhotoDeleter.deleteAllByTempPostId(tempPostId);
		temporaryPostCategoryDeleter.deleteAllByTemporaryPostId(tempPostId);

		// 5. 임시 게시글 삭제
		temporaryPostDeleter.delete(temporaryPost);

		// 6. 새 데이터 추가
		Post post = postAppender.appendNormalPost(userId, temporaryPost.getBoardId(), title, content);
		postPhotoAppender.appendAll(post.getId(), imageUrls);
		postCategoryAppender.appendAll(post.getId(), categoryIds);

		return post.getId();
	}

	@Transactional
	public Long updateDraftPost(
		Long tempPostId,
		String title,
		String content,
		List<String> categoryNames,
		List<MultipartFile> images
	) throws ImageException {
		// 1. 유저 및 임시 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		TemporaryPost temporaryPost = temporaryPostFinder.find(tempPostId);
		temporaryPost.validateAuthor(userId);

		// 2. 카테고리 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryResolver.validateMatching(categoryIds, temporaryPost.getBoardId());

		// 3. 이미지 처리
		// 3.1 새 이미지 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 3.2 기존 이미지 삭제
		List<String> deletePhotos = temporaryPhotoFinder.findAllPhotoUrls(tempPostId);
		s3Uploader.deleteFiles(deletePhotos);

		// 4. 연관 데이터 갱신
		// 4.1 기존 데이터 삭제
		temporaryPhotoDeleter.deleteAllByTempPostId(tempPostId);
		temporaryPostCategoryDeleter.deleteAllByTemporaryPostId(tempPostId);

		// 4.2 새 데이터 추가
		temporaryPostCategoryAppender.appendAll(temporaryPost.getId(), categoryIds);
		temporaryPhotoAppender.appendAll(temporaryPost.getId(), imageUrls);

		// 5. 임시 게사글 내용 업데이트
		temporaryPostUpdater.update(temporaryPost, title, content);
		return tempPostId;
	}

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

		// 4. 기존에 좋아요가 2개였다면 Trending 게시판에 추가
		//  TRENDING_LIKE_THRESHOLD 추가
		boolean isTrending = trendingPostFinder.existsByPostId(postId);
		if (!isTrending) {
			trendingPostAppender.append(postId);
		}

		// 5. post의 likeCount + 1
		postUpdater.increaseLikeCount(post);

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

		// 4. 기존에 좋아요가 3개 였다면 Trending 게시판에서 제거
		boolean isTrending = trendingPostFinder.existsByPostId(postId);

		if (isTrending) {
			// private static final int TRENDING_LIKE_THRESHOLD = 3;

			trendingPostDeleter.deleteByPostId(postId);
		}
		// 3. post의 likeCount - 1
		postUpdater.decreaseLikeCount(post);

	}

	@Transactional
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

		// 스크랩 중복 검증
		postValidator.validateDuplicatedScrap(postId, userId);

		// 게시글 스크랩 수 추가
		postUpdater.increaseScrapCount(post);

		// 스크랩 데이터 추가
		postScrapAppender.append(postId, userId);
	}

	@Transactional
	public void unscrapPost(Long postId) {
		// 1. 유저 및 게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		Post post = postFinder.find(postId);
		post.validatePublish();

		// 게시글 스크랩 수 감소
		postUpdater.decreaseScrapCount(post);

		PostScrap postScrap = postScrapFinder.find(userId, postId);
		// 스크랩 데이터 삭제
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

	public Slice<PostThumbnailWithBoardName> searchAllPosts(String keyword, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);

		// 검색 결과 리턴
		Slice<Post> searchedPosts = postFinder.searchAllPosts(keyword, page);

		return postDtoMapper.toPostThumbnailsWithBoardName(searchedPosts, userId);
	}

	public Slice<PostThumbnailWithBoardName> searchBoardPosts(String keyword, Long boardId, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);

		Slice<Post> searchedPosts = postFinder.searchBoardPosts(keyword, boardId, page);

		return postDtoMapper.toPostThumbnailsWithBoardName(searchedPosts, userId);
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