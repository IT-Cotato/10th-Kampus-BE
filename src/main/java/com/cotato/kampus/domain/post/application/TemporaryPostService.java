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
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.TempPostDetails;
import com.cotato.kampus.domain.post.domain.TempPostThumbnail;
import com.cotato.kampus.domain.post.domain.TemporaryPhoto;
import com.cotato.kampus.domain.post.domain.TemporaryPost;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPhotoManager;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPhotoFinder;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostManager;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostCategoryManager;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostCategoryFinder;
import com.cotato.kampus.domain.post.implement.TemporaryPost.TemporaryPostFinder;
import com.cotato.kampus.domain.post.implement.post.PostAppender;
import com.cotato.kampus.domain.post.implement.postCategory.PostCategoryAppender;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoAppender;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemporaryPostService {

	private final ApiUserResolver apiUserResolver;

	private final ImageValidator imageValidator;
	private final S3Uploader s3Uploader;
	private static final String TEMP_POST_IMAGE_FOLDER = "temp_post";

	private final BoardFinder boardFinder;
	private final BoardValidator boardValidator;
	private final BoardCategoryValidator boardCategoryValidator;
	private final CategoryFinder categoryFinder;
	private final PostCategoryAppender postCategoryAppender;
	private final PostAppender postAppender;
	private final PostPhotoAppender postPhotoAppender;

	private final TemporaryPostManager temporaryPostManager;
	private final TemporaryPostFinder temporaryPostFinder;
	private final TemporaryPhotoFinder temporaryPhotoFinder;
	private final TemporaryPhotoManager temporaryPhotoManager;
	private final TemporaryPostCategoryManager temporaryPostCategoryManager;
	private final TemporaryPostCategoryFinder temporaryPostCategoryFinder;

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
		board.validateActive();
		boardValidator.validatePostCreationAccess(user, board);

		// 2. 카테고리 검증
		List<Long> categoryIds = categoryNames.stream()
			.map(categoryFinder::find)
			.map(Category::getId)
			.toList();
		boardCategoryValidator.validateMatching(categoryIds, boardId);

		// 3. 이미지 처리
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, TEMP_POST_IMAGE_FOLDER);

		// 4. 임시 게시글 생성
		TemporaryPost temporaryPost = temporaryPostManager.append(user.id(), boardId, title, content);

		// 5. 관련 데이터 추가
		temporaryPostCategoryManager.appendAll(temporaryPost.getId(), categoryIds);
		temporaryPhotoManager.appendAll(temporaryPost.getId(), imageUrls);

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
		temporaryPhotoManager.deleteAllByTempPostIds(tempPostIds);
		temporaryPostCategoryManager.deleteAllByTemporaryPostIds(tempPostIds);

		// 4. 임시 게시글 삭제
		temporaryPostManager.deleteAllByIds(tempPostIds);
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
		temporaryPhotoManager.deleteAllByTempPostIds(tempPostIds);
		temporaryPostCategoryManager.deleteAllByTemporaryPostIds(tempPostIds);

		// 5. 임시 게시글 삭제
		temporaryPostManager.deleteAllByUser(userId);
	}

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

	public TempPostDetails findTempPostDetail(Long postDraftId) {
		// 1. 유저 및 임시게시글 조회/검증
		Long userId = apiUserResolver.getCurrentUserId();
		TemporaryPost temporaryPost = temporaryPostFinder.find(postDraftId);
		temporaryPost.validateAuthor(userId);

		List<TemporaryPhoto> tempPhotos = temporaryPhotoFinder.findAllByTempPostId(postDraftId);
		String boardName = boardFinder.findBoard(temporaryPost.getBoardId()).getBoardName();
		List<Long> categoryIds = temporaryPostCategoryFinder.findCategoryIdsByTempPostId(postDraftId);
		List<Category> categories = categoryFinder.findAllByIds(categoryIds);
		return TempPostDetails.of(temporaryPost, boardName, tempPhotos, categories);
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
		boardCategoryValidator.validateMatching(categoryIds, temporaryPost.getBoardId());

		// 3. 이미지 처리
		// 3.1 새 이미지 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, TEMP_POST_IMAGE_FOLDER);

		// 3.2 기존 이미지 삭제
		List<String> photoUrls = temporaryPhotoFinder.findAllPhotoUrls(tempPostId);
		s3Uploader.deleteFiles(photoUrls);

		// 4. 임시 게시글 관련 데이터 삭제
		temporaryPhotoManager.deleteAllByTempPostId(tempPostId);
		temporaryPostCategoryManager.deleteAllByTemporaryPostId(tempPostId);

		// 5. 임시 게시글 삭제
		temporaryPostManager.delete(temporaryPost);

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
		boardCategoryValidator.validateMatching(categoryIds, temporaryPost.getBoardId());

		// 3. 이미지 처리
		// 3.1 새 이미지 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, TEMP_POST_IMAGE_FOLDER);

		// 3.2 기존 이미지 삭제
		List<String> deletePhotos = temporaryPhotoFinder.findAllPhotoUrls(tempPostId);
		s3Uploader.deleteFiles(deletePhotos);

		// 4. 연관 데이터 갱신
		// 4.1 기존 데이터 삭제
		temporaryPhotoManager.deleteAllByTempPostId(tempPostId);
		temporaryPostCategoryManager.deleteAllByTemporaryPostId(tempPostId);

		// 4.2 새 데이터 추가
		temporaryPostCategoryManager.appendAll(temporaryPost.getId(), categoryIds);
		temporaryPhotoManager.appendAll(temporaryPost.getId(), imageUrls);

		// 5. 임시 게사글 내용 업데이트
		temporaryPostManager.update(temporaryPost, title, content);
		return tempPostId;
	}

	public Integer findTempPostCount() {
		Long userId = apiUserResolver.getCurrentUserId();
		return temporaryPostFinder.findCountByUserId(userId);
	}
}
