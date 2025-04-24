package com.cotato.kampus.domain.post.implement.postImage;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.implement.port.PostDraftPhotoRepository;
import com.cotato.kampus.domain.post.implement.port.PostPhotoRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhotoDeleter {

	private final PostPhotoRepository postPhotoRepository;

	public void deletePostPhotos(Long postId){
		postPhotoRepository.deleteAllByPostId(postId);
	}
}
