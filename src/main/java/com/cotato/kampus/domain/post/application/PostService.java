package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.post.dto.AnonymousOrPostAuthor;
import com.cotato.kampus.domain.post.dto.MyPostWithPhoto;
import com.cotato.kampus.domain.post.dto.PostDetails;
import com.cotato.kampus.domain.post.dto.PostDto;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostService {

	private final PostAppender postAppender;
	private final PostDeleter postDeleter;
	private final PostImageAppender postImageAppender;
	private final PostFinder postFinder;
	private final PostUpdater postUpdater;
	private final PostAuthorResolver postAuthorResolver;
	private static final String POST_IMAGE_FOLDER = "post";
	private final PostImageFinder postImageFinder;
	private final PostImageUpdater postImageUpdater;
	private final PostScrapUpdater postScrapUpdater;
	private final ApiUserResolver apiUserResolver;
	private final S3Uploader s3Uploader;
	private final UserValidator userValidator;
	private final ImageValidator imageValidator;
	private final PostScrapValidator postScrapValidator;

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		PostCategory postCategory,
		Anonymity anonymity,
		List<MultipartFile> images
	) throws ImageException {

		// 유효한 이미지만 필터링
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, POST_IMAGE_FOLDER);

		// 게시글 추가
		Long postId = postAppender.append(boardId, title, content, postCategory, anonymity);

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
		Long authorId = postAuthorResolver.getAuthorId(postId);

		userValidator.validatePostAuthor(authorId, userId);

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
	public void updatePost(Long postId, String title, String content, PostCategory postCategory, Anonymity anonymity,
		List<MultipartFile> images) throws ImageException {
		// 1. Post Author 검증
		Long userId = apiUserResolver.getUserId();
		userValidator.validatePostAuthor(postId, userId);

		// 2. Post 업데이트
		postUpdater.updatePost(postId, title, content, postCategory, anonymity);

		// 3. Post Images 업데이트
		postImageUpdater.updatePostImages(postId, images);
	}

	public Slice<MyPostWithPhoto> findUserPosts(int page){
		return postFinder.findUserPosts(page);
	}

	@Transactional
	public void scrapPost(Long postId){
		// 스크랩 가능 여부 검증
		Long userId = apiUserResolver.getUserId();
		postScrapValidator.validatePostScrap(postId, userId);

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
}
