package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.dto.AnonymousOrPostAuthor;
import com.cotato.kampus.domain.post.dto.MyPostWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDetails;
import com.cotato.kampus.domain.post.dto.PostDraftDetails;
import com.cotato.kampus.domain.post.dto.PostDraftDto;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;
import com.cotato.kampus.domain.post.enums.PostCategory;
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

	private final PostImageAppender postImageAppender;
	private final PostImageFinder postImageFinder;
	private final PostImageUpdater postImageUpdater;
	private final PostImageDeleter postImageDeleter;

	private final PostScrapUpdater postScrapUpdater;
	private final PostAuthorResolver postAuthorResolver;
	private final ApiUserResolver apiUserResolver;
	private final S3Uploader s3Uploader;

	private final ImageValidator imageValidator;
	private final PostLikeAppender postLikeAppender;
	private final PostLikeValidator postLikeValidator;
	private final PostValidator postValidator;

	private static final String POST_IMAGE_FOLDER = "post";

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		PostCategory postCategory,
		List<MultipartFile> images
	) throws ImageException {

		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 유효한 이미지만 필터링
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 게시글 추가
		Long postId = postAppender.append(userId, boardId, title, content, postCategory);

		// 게시글 이미지 추가
		if (!imageUrls.isEmpty()) {
			postImageAppender.appendAll(postId, imageUrls);
		}

		return postId;
	}

	@Transactional
	public Long deletePost(Long postId) {
		// 작성자 검증: 현재 사용자가 게시글 작성자인지 확인
		Long userId = apiUserResolver.getUserId();
		postValidator.validatePostOwner(postId, userId);

		// 이미지 조회
		List<String> imageUrls = postImageFinder.findPostPhotos(postId);

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostPhoto 삭제
		postImageDeleter.deletePostPhotos(postId);

		// 게시글 삭제
		postDeleter.delete(postId);

		return postId;
	}

	public Slice<PostWithPhotos> findPosts(Long boardId, int page) {
		return postFinder.findPosts(boardId, page);
	}

	public PostDetails findPostDetail(Long postId) {
		// 1. Post 조회하여 Dto에 저장
		PostDto postDto = postFinder.findPost(postId);

		// 2. Post의 이미지 조회
		List<String> postPhotos = postImageFinder.findPostPhotos(postId);

		// 3. 유저가 익명인지 아닌지 조회 + 게시글 작성자인지 확인
		AnonymousOrPostAuthor author = postAuthorResolver.getAuthor(postDto);

		// 4. 게시글 세부 내역 리턴
		return PostDetails.of(author, postDto, postPhotos);
	}

	@Transactional
	public void updatePost(Long postId, String title, String content, PostCategory postCategory,
		List<MultipartFile> images) throws ImageException {
		// 1. Post Author 검증
		Long userId = apiUserResolver.getUserId();
		postValidator.validatePostOwner(postId, userId);

		// 2. Post 업데이트
		postUpdater.updatePost(postId, title, content, postCategory);

		// 3. Post Images 업데이트
		postImageUpdater.updatePostImages(postId, images);
	}

	@Transactional
	public Long draftPost(
		Long boardId,
		String title,
		String content,
		PostCategory postCategory,
		List<MultipartFile> images
	) throws ImageException {
		// 유효한 이미지만 필터링
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 임시 저장글 추가
		Long postDraftId = postAppender.draft(boardId, title, content, postCategory);

		// 임시 저장 이미지 추가
		if (!imageUrls.isEmpty()) {
			postImageAppender.appendAllDraftImage(postDraftId, imageUrls);
		}

		return postDraftId;
	}

	@Transactional
	public void deleteDraftPosts(List<Long> postDraftIds){
		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 작성자 검증
		postDraftIds.forEach(postDraftId -> postValidator.validateDraftPostDelete(postDraftId, userId));

		// 이미지 조회
		List<String> imageUrls = postImageFinder.findAllDraftPhotos(postDraftIds);

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostDraftPhoto 삭제
		postImageDeleter.deletePostDraftPhotos(imageUrls);

		// 삭제 처리
		postDeleter.deleteDraftAll(postDraftIds);

	}

	@Transactional
	public void deleteAllDraftPost(Long boardId){
		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 임시 저장 게시글 조회
		List<Long> draftPostIds = postFinder.getPostDraftIdsByBoardAndUser(boardId, userId);
		List<String> imageUrls = postImageFinder.findAllDraftPhotos(draftPostIds);

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostDraftPhoto 삭제
		postImageDeleter.deletePostDraftPhotos(imageUrls);

		// 임시저장 글 삭제
		postDeleter.deleteDraftAll(draftPostIds);
	}

	@Transactional
	public Slice<PostDraftWithPhoto> findPostDrafts(Long boardId, int page) {

		Long userId = apiUserResolver.getUserId();

		return postFinder.findPostDrafts(boardId, userId, page);
	}

	@Transactional
	public PostDraftDetails findDraftDetail(Long postDraftId){

		PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);

		List<String> postDraftPhotos = postImageFinder.findAllDraftPhotos(postDraftId);

		return PostDraftDetails.of(postDraftDto, postDraftPhotos);
	}

	@Transactional
	public Long publishDraftPost(
		Long postDraftId,
		String title,
		String content,
		PostCategory postCategory,
		List<String> deletedImageUrls,
		List<MultipartFile> newImages) throws ImageException {

		// 1. 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 2. 임시 저장 게시글 정보 조회
		PostDraftDto postDraftDto = postFinder.findPostDraftDto(postDraftId);

		// 3. 기존 임시 저장 이미지 URL 목록 조회
		List<String> existingImageUrls = postImageFinder.findAllDraftPhotos(postDraftId);

		// 4. 게시글 생성 (임시 저장된 게시글에서 필요한 정보로 새로운 게시글을 생성)
		Long postId = postAppender.append(userId, postDraftDto.boardId(), title, content, postCategory);

		// 5. 새로 추가된 이미지가 있다면 유효성 검증 후 S3에 업로드
		List<MultipartFile> validImages = imageValidator.filterValidImages(newImages);
		List<String> newImageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 6. 삭제할 이미지가 유효한지 검증
		imageValidator.validateDeletableImages(existingImageUrls, deletedImageUrls);

		// 7. 기존 이미지에서 삭제할 이미지 제외하고, 새로 추가된 이미지 합쳐서 최종 이미지 리스트 생성
		List<String> finalImages = postImageUpdater.getUpdateImageUrls(existingImageUrls, deletedImageUrls, newImageUrls);

		// 8. 최종 이미지가 있으면 게시글에 이미지 추가
		if (!finalImages.isEmpty()) {
			postImageAppender.appendAll(postId, finalImages);
		}

		return postId;
	}

	@Transactional
	public void likePost(Long postId) {
		// 1. userId 조회
		Long userId = apiUserResolver.getUserId();

		// 2. 좋아요 제약 조건 검증(자신의 게시글, 이미 좋아요한 게시글)
		postLikeValidator.validatePostLike(postId, userId);

		// 3. 좋아요 추가
		postLikeAppender.appendPostLike(postId, userId);

		// 4. post의 likes + 1
		postUpdater.increasePostLike(postId);
	}

	public Slice<MyPostWithPhoto> findUserPosts(int page) {
		return postFinder.findUserPosts(page);
	}

	@Transactional
	public void scrapPost(Long postId){
		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 스크랩 가능 여부 검증
		postValidator.validatePostScrap(postId, userId);

		// 게시글 스크랩 수 추가
		postUpdater.increaseScraps(postId);

		// 스크랩 데이터 추가
		postScrapUpdater.append(postId, userId);
	}

	@Transactional
	public void unscrapPost(Long postId){
		// 유저 조회
		Long userId = apiUserResolver.getUserId();

		// 게시글 스크랩 수 감소
		postUpdater.decreaseScraps(postId);

		// 스크랩 데이터 삭제
		postScrapUpdater.delete(postId, userId);
	}

	public Slice<MyPostWithPhoto> findUserScrapedPosts(int page){
		return postFinder.findUserScrapedPosts(page);
	}


	public void validateCategoryForBoard(boolean requiresCategory, PostCategory postCategory) {
		// 카테고리 필수인데 값이 없는 경우 -> 예외 발생
		if(requiresCategory && (postCategory == null))
			throw new AppException(ErrorCode.CATEGORY_REQUIRED);

		// 카테고리 필요 없는데 값이 들어온 경우 -> 예외 발생
		if(!requiresCategory && (postCategory != null))
			throw new AppException(ErrorCode.CATEGORY_NOT_ALLOWED);
	}

}