package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.enums.Anonymity;
import com.cotato.kampus.domain.product.application.PostDeleter;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostService {

	private final S3Uploader s3Uploader;
	private final PostAppender postAppender;
	private final PostDeleter postDeleter;
	private final PostImageAppender postImageAppender;
	private static final String PRODUCT_IMAGE_FOLDER = "post";
	private final ApiUserResolver apiUserResolver;
	private final PostFinder postFinder;

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		String postCategory,
		Anonymity anonymity,
		List<MultipartFile> images
	) throws ImageException {
		// s3에 이미지 업로드
		List<String> imageUrls = (images == null || images.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(
				images.stream()
					.filter(image -> image != null && image.getOriginalFilename() != null && !image.getOriginalFilename().isEmpty())
					.toList(),
				PRODUCT_IMAGE_FOLDER
			);

		// 게시글 추가
		Long postId = postAppender.append(boardId, title, content, postCategory, anonymity);

		// 게시글 이미지 추가
		if(!imageUrls.isEmpty()) {
			postImageAppender.appendAll(postId, imageUrls);
		}

		return postId;
	}

	@Transactional
	public Long deletePost(Long postId){
		// 작성자 검증: 현재 사용자가 게시글 작성자인지 확인
		Long userId = apiUserResolver.getUserId();
		Long authorId = postFinder.getAuthorId(postId);

		// 작성자가 아닌 경우 예외 처리
		if(userId != authorId)
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);

		// 게시글 삭제
		postDeleter.delete(postId);

		return postId;
	}
}
