package com.cotato.kampus.domain.post.implement.postImage;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostPhotoRepository;
import com.cotato.kampus.domain.post.domain.PostPhoto;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PostPhotoAppender {

	private final PostPhotoRepository postPhotoRepository;

	public List<PostPhoto> appendAll(Long postId, List<String> photoUrls){
		List<PostPhoto> postPhotos = IntStream.range(0, photoUrls.size())
				.mapToObj(i -> PostPhoto.builder()
					.postId(postId)
					.photoUrl(photoUrls.get(i))
					.order(i)
					.build())
				.toList();

		return postPhotoRepository.saveAll(postPhotos);
	}
}