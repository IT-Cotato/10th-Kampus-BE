package com.cotato.kampus.domain.post.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.post.dao.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageUpdater {

	private final PostPhotoRepository postPhotoRepository;
	private final S3Uploader s3Uploader;
	private static final String POST_IMAGE_FOLDER = "post";

	public void updatePostImages(Long postId, List<MultipartFile> images) throws ImageException {
		postPhotoRepository.deleteAllByPostId(postId);

		// 이미지가 있는 경우에만 업데이트
		if (!images.isEmpty()) {
			List<String> postImages = s3Uploader.uploadFiles(images, POST_IMAGE_FOLDER);

			List<PostPhoto> postPhotos = postImages.stream()
				.map((postImage) -> PostPhoto.builder()
					.postId(postId)
					.photoUrl(postImage)
					.build()
				).toList();

			postPhotoRepository.saveAll(postPhotos);
		}
	}

	public List<String> getUpdateImageUrls(List<String> originalImageUrls, List<String> imageUrlsToDelete, List<String> newImageUrls){

		List<String> remainingImages = originalImageUrls.stream()
			.filter(image -> !imageUrlsToDelete.contains(image))
			.toList();

		List<String> finalImages = new ArrayList<>();
		finalImages.addAll(remainingImages);
		finalImages.addAll(newImageUrls);

		return finalImages;
	}
}