package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.dao.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostDraftPhoto;
import com.cotato.kampus.domain.post.domain.PostPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageAppender {

	private final PostPhotoRepository postPhotoRepository;
	private final PostDraftPhotoRepository postDraftPhotoRepository;

	@Transactional
	public void appendAll(Long postId, List<String> imageUrls){
			imageUrls.forEach(imageUrl -> {
				PostPhoto postPhoto = PostPhoto.builder()
					.postId(postId)
					.photoUrl(imageUrl)
					.build();

				postPhotoRepository.save(postPhoto);
			});
	}

	@Transactional
	public void appendAllDraftImage(Long postDraftId, List<String> imageUrls){
		imageUrls.forEach(imageUrl -> {
			PostDraftPhoto postDraftPhoto = PostDraftPhoto.builder()
				.postDraftId(postDraftId)
				.photoUrl(imageUrl)
				.build();

			postDraftPhotoRepository.save(postDraftPhoto);
		});
	}
}