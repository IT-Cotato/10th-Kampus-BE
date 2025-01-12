package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostService {

	private final S3Uploader s3Uploader;
	private final PostAppender postAppender;
	private final PostImageAppender postImageAppender;
	private static final String PRODUCT_IMAGE_FOLDER = "post";

	@Transactional
	public Long createPost(
		Long boardId,
		String title,
		String content,
		String postCategory,
		List<MultipartFile> images
	) throws ImageException {
		// s3에 이미지 업로드
		List<String> imageUrls = s3Uploader.uploadFiles(images, PRODUCT_IMAGE_FOLDER);

		// 게시글 추가
		Long postId = postAppender.append(boardId, title, content, postCategory);

		// 게시글 이미지 추가
		postImageAppender.appendAll(postId, imageUrls);

		return postId;
	}
}
