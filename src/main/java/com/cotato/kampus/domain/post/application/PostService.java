package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.BoardFinder;
import com.cotato.kampus.domain.board.implement.BoardValidator;
import com.cotato.kampus.domain.board.implement.BoardCategoryResolver;
import com.cotato.kampus.domain.comment.application.CommentDeleter;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.dto.CardNewsPreview;
import com.cotato.kampus.domain.post.dto.MyPostWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDetails;
import com.cotato.kampus.domain.post.dto.PostDraftDetails;
import com.cotato.kampus.domain.post.dto.PostDraftDto;
import com.cotato.kampus.domain.post.dto.PostDraftSliceFindDto;
import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.PostSearchHistoryList;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.dto.SearchedPost;
import com.cotato.kampus.domain.post.dto.TrendingPostPreview;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
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
	private final PostImageUpdater postImageUpdater;
	private final PostPhotoDeleter postPhotoDeleter;

	private final PostScrapUpdater postScrapUpdater;
	private final PostScrapFinder postScrapFinder;
	private final ApiUserResolver apiUserResolver;
	private final S3Uploader s3Uploader;

	private final ImageValidator imageValidator;
	private final PostLikeUpdater postLikeUpdater;
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

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		List<MultipartFile> images,
		List<String> categories
	) throws ImageException {
		// 게시판, 유저 조회
		Board board = boardFinder.findBoard(boardId);
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		// 게시판 검증
		boardValidator.validateBoardIsActive(board);
		boardValidator.validatePostCreationAccess(userDto, board);

		// 게시글 추가
		Long postId = postAppender.append(userDto.id(), board.getId(), title, content);

		// 유효한 이미지 필터링 & S3 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// PostPhoto 추가
		postPhotoAppender.appendAll(postId, imageUrls);

		// 카테고리 조회, 검증
		List<Long> categoryIds = boardCategoryResolver.resolveCategoryIds(categories, boardId);

		// PostCategory 추가
		postCategoryAppender.appendAll(postId, categoryIds);

		return postId;
	}

	@Transactional
	public Long deletePost(Long postId) {
		// 작성자 검증: 현재 사용자가 게시글 작성자인지 확인
		Long userId = apiUserResolver.getCurrentUserId();
		postValidator.validatePostOwner(postId, userId);

		// 이미지 조회, 삭제
		List<String> imageUrls = postPhotoFinder.findPostPhotos(postId);
		s3Uploader.deleteFiles(imageUrls);

		// PostPhoto 삭제
		postPhotoDeleter.deletePostPhotos(postId);

		// PostCategory 삭제
		postCategoryDeleter.deleteAllByPostId(postId);

		// PostLike, PostScrap 삭제
		postLikeUpdater.deleteAllByPostId(postId);
		postScrapUpdater.deleteAllByPostId(postId);

		// Comment, CommentLike 삭제
		commentDeleter.deleteAllByPostId(postId);

		// 게시글 삭제
		postDeleter.delete(postId);

		return postId;
	}

	public Slice<PostWithPhotos> findPosts(Long boardId, int page, PostSortType sortType, String categoryName) {
		// 현재 사용자 정보 조회
		UserDto user = apiUserResolver.getCurrentUserDto();
		Board board = boardFinder.findBoard(boardId);

		// 게시판 접근 권한 검증
		boardValidator.validateBoardIsActive(board);
		boardValidator.validateUniversityAccess(user, board);

		if (categoryName != null) {
			boardValidator.isCategoryEnabled(board);
		}

		return postFinder.findPostsByCategory(boardId, page, sortType, categoryName);
	}

	public Slice<CardNewsPreview> findAllCardNews(int page) {
		// 현재 사용자 정보 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 카드뉴스 조회
		return postFinder.findAllCardNews(userId, page);
	}

	public Slice<TrendingPostPreview> findTrendingPosts(int page) {
		// 유저 정보 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();
		Long userUnivId = userDto.universityId();

		// Trending 게시글 조회 (타 대학 게시글 제외)
		return postFinder.findAllTrendingPosts(userUnivId, page);
	}

	public PostDetails findPostDetail(Long postId) {
		// 1. Post 조회하여 Dto에 저장
		PostDto postDto = postFinder.findPost(postId);

		// 2. Post의 이미지 조회
		List<String> postPhotos = postPhotoFinder.findPostPhotos(postId);

		// 3. 유저 조회
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		// 4. 게시글 작성자 여부, 좋아요 여부, 스크랩 여부 조회
		boolean isAuthor = postDto.userId().equals(userDto.id());
		boolean isLiked = postLikeFinder.isPostLikedByUser(userDto.id(), postId);
		boolean isScrapped = postScrapFinder.isPostScrappedByUser(userDto.id(), postId);

		// 5. 게시글 세부 내역 리턴
		return PostDetails.of(postDto, postPhotos, isAuthor, isLiked, isScrapped);
	}

	@Transactional
	public void updatePost(
		Long postId,
		String title,
		String content,
		List<String> categories,
		List<MultipartFile> images
	) throws ImageException {
		// 1. Post Author 검증
		Long userId = apiUserResolver.getCurrentUserId();
		postValidator.validatePostOwner(postId, userId);

		// 2. PostCategory 업데이트
		postCategoryDeleter.deleteAllByPostId(postId);
		if (!categories.isEmpty()) {
			// 게시판이 카테고리 쓰는지 확인
			PostDto postDto = postFinder.findPost(postId);
			Board board = boardFinder.findBoard(postDto.boardId());
			boardValidator.isCategoryEnabled(board);

			// 카테고리 조회, 검증
			List<Long> categoryIds = boardCategoryResolver.resolveCategoryIds(categories, board.getId());

			// PostCategory 추가
			postCategoryAppender.appendAll(postId, categoryIds);
		}

		// 3. 기존 PostPhoto 삭제
		List<String> deletePhotos = postPhotoFinder.findPostPhotos(postId);
		s3Uploader.deleteFiles(deletePhotos);
		postPhotoDeleter.deletePostPhotos(postId);

		// 4. 유효한 이미지 필터링 & S3 업로드
		List<MultipartFile> validphotos = imageValidator.filterValidImages(images);
		List<String> photoUrls = (validphotos.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validphotos, POST_IMAGE_FOLDER);

		// 5. PostPhoto 추가
		postPhotoAppender.appendAll(postId, photoUrls);

		// 4. Post 업데이트
		postUpdater.updatePost(postId, title, content);

	}

	@Transactional
	public Long draftPost(
		Long boardId,
		String title,
		String content,
		List<String> categories,
		List<MultipartFile> images
	) throws ImageException {
		// 게시판, 유저 조회
		Board board = boardFinder.findBoard(boardId);
		UserDto userDto = apiUserResolver.getCurrentUserDto();

		// 게시판 검증
		boardValidator.validateBoardIsActive(board);
		boardValidator.validatePostCreationAccess(userDto, board);

		// PostDraft 추가
		Long postDraftId = postAppender.draft(boardId, title, content);

		// 유효한 이미지 필터링 & S3 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// PostDraftPhoto 추가
		postPhotoAppender.appendAllDraftImage(postDraftId, imageUrls);

		// 카테고리 검증, PostDraftCategory 추가
		List<Long> categoryIds = boardCategoryResolver.resolveCategoryIds(categories, boardId);
		postCategoryAppender.appendAllDraftCategory(postDraftId, categoryIds);

		return postDraftId;
	}

	@Transactional
	public void deleteSelectedDraftPosts(List<Long> postDraftIds) {
		// 유저 조회, 검증
		Long userId = apiUserResolver.getCurrentUserId();
		postDraftIds.forEach(postDraftId -> {
			PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);
			postValidator.validatePostDraftOwner(postDraftDto, userId);
		});

		// 이미지 조회, 삭제
		List<String> imageUrls = postPhotoFinder.findAllDraftPhotos(postDraftIds);
		s3Uploader.deleteFiles(imageUrls);

		// PostDraftPhoto 삭제
		postPhotoDeleter.deletePostDraftPhotos(imageUrls);

		// PostDraftCategory 삭제
		postCategoryDeleter.deleteAllByPostDraftIds(postDraftIds);

		// PostDraft 삭제
		postDeleter.deleteDraftAll(postDraftIds);

	}

	@Transactional
	public void deleteAllDraftPost() {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 임시 저장 게시글 조회
		List<Long> draftPostIds = postFinder.getPostDraftIdsByBoardAndUser(userId);
		List<String> imageUrls = postPhotoFinder.findAllDraftPhotos(draftPostIds);

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostDraftPhoto 삭제
		postPhotoDeleter.deletePostDraftPhotos(imageUrls);

		// 임시저장 글 삭제
		postDeleter.deleteDraftAll(draftPostIds);
	}

	@Transactional
	public PostDraftSliceFindDto findPostDrafts(int page) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 임시 저장 글 조회
		Slice<PostDraftWithPhoto> postDrafts = postFinder.findPostDrafts(userId, page);
		int count = postFinder.findDraftsCount(userId);

		return PostDraftSliceFindDto.from(postDrafts, count);
	}

	@Transactional
	public PostDraftDetails findDraftDetail(Long postDraftId) {

		PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);

		List<String> postDraftPhotos = postPhotoFinder.findAllDraftPhotos(postDraftId);

		return PostDraftDetails.of(postDraftDto, postDraftPhotos);
	}

	@Transactional
	public Long publishDraftPost(Long postDraftId) {
		// 1. 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 임시 저장 게시글 정보 조회
		PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);

		// 게시글 필수값 유효성 검증
		postValidator.validatePublishable(postDraftDto);

		// 3. 게시글 생성 (임시 저장된 게시글에서 필요한 정보로 새로운 게시글을 생성)
		Long postId = postAppender.append(userId, postDraftDto.boardId(), postDraftDto.title(), postDraftDto.content());

		// 기존 이미지로 PostPhoto 생성
		List<String> imageUrls = postPhotoFinder.findAllDraftPhotos(postDraftId);
		postPhotoAppender.appendAll(postId, imageUrls);
		//
		// // PostDraftPhoto 삭제
		// postPhotoDeleter.deletePostDraftPhotos(imageUrls);

		// 기존 카테고리로 PostCategory 생성
		List<Long> categoryIds = postCategoryFinder.findAllCategoryId(postDraftId);
		postCategoryAppender.appendAll(postId, categoryIds);
		//
		// // PostDraftCategory 삭제
		// postCategoryDeleter.deleteAllByPostDraftId(postDraftId);
		//
		// // 임시저장글 삭제
		// postDeleter.deleteDraftPost(postDraftId);

		return postId;
	}

	@Transactional
	public Long updateDraftPost(
		Long postDraftId,
		String title,
		String content,
		List<String> categories,
		List<MultipartFile> images
	) throws ImageException {
		// PostDraft, 유저 조회
		PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);
		Long userId = apiUserResolver.getCurrentUserId();

		// 작성자 검증
		postValidator.validatePostDraftOwner(postDraftDto, userId);

		// 이미지 조회, 삭제
		List<String> deletePhotos = postPhotoFinder.findAllDraftPhotos(postDraftId);
		s3Uploader.deleteFiles(deletePhotos);

		// PostDraftPhoto 삭제
		postPhotoDeleter.deletePostDraftPhotos(deletePhotos);

		// PostDraftCategory 삭제
		postCategoryDeleter.deleteAllByPostDraftId(postDraftId);

		// 유효한 이미지 필터링 & S3 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// PostDraftPhoto 추가
		postPhotoAppender.appendAllDraftImage(postDraftId, imageUrls);

		// 카테고리 검증, PostDraftCategory 추가
		List<Long> categoryIds = boardCategoryResolver.resolveCategoryIds(categories, postDraftDto.boardId());
		postCategoryAppender.appendAllDraftCategory(postDraftId, categoryIds);

		postUpdater.updateDraftPost(postDraftId, title, content);

		return postDraftId;
	}

	@Transactional
	public void likePost(Long postId) {
		// 1. userId 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 좋아요 제약 조건 검증(이미 좋아요한 게시글)
		postLikeValidator.validateDuplicateLike(postId, userId);

		// 3. 좋아요 추가
		postLikeUpdater.append(postId, userId);

		// 4. 기존에 좋아요가 2개였다면 Trending 게시판에 추가
		trendingPostAppender.appendTrendingPost(postId);

		// 5. post의 likes + 1
		postUpdater.increasePostLike(postId);

	}

	@Transactional
	public void unlikePost(Long postId) {
		// 1. userId 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 2. 좋아요 삭제
		postLikeUpdater.delete(postId, userId);

		// 4. 기존에 좋아요가 3개 였다면 Trending 게시판에서 제거
		postDeleter.deleteTrendingPost(postId);

		// 3. post의 likes - 1
		postUpdater.decreasePostLike(postId);

	}

	public Slice<MyPostWithPhoto> findUserPosts(int page) {
		return postFinder.findUserPosts(page);
	}

	@Transactional
	public void scrapPost(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 스크랩 중복 검증
		postValidator.validateDuplicatedScrap(postId, userId);

		// 게시글 스크랩 수 추가
		postUpdater.increaseScraps(postId);

		// 스크랩 데이터 추가
		postScrapUpdater.append(postId, userId);
	}

	@Transactional
	public void unscrapPost(Long postId) {
		// 유저 조회
		Long userId = apiUserResolver.getCurrentUserId();

		// 게시글 스크랩 수 감소
		postUpdater.decreaseScraps(postId);

		// 스크랩 데이터 삭제
		postScrapUpdater.delete(postId, userId);
	}

	public Slice<MyPostWithPhoto> findUserScrapedPosts(int page) {
		return postFinder.findUserScrapedPosts(page);
	}

	public void validateCategoryForBoard(boolean requiresCategory, PostCategory postCategory) {
		// 카테고리 필요 없는데 값이 들어온 경우 -> 예외 발생
		if (!requiresCategory && (postCategory != null))
			throw new AppException(ErrorCode.CATEGORY_NOT_ALLOWED);
	}

	public Slice<SearchedPost> searchAllPosts(String keyword, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);
		// 검색 결과 리턴
		return postFinder.searchAllPosts(keyword, page);
	}

	public Slice<SearchedPost> searchBoardPosts(String keyword, Long boardId, int page) {
		// 최대 5개 까지 키워드 저장
		Long userId = apiUserResolver.getCurrentUserId();
		postSearchHistoryAppender.append(userId, keyword);
		return postFinder.searchBoardPosts(keyword, boardId, page);
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