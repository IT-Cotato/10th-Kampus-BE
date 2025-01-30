package com.cotato.kampus.domain.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.post.dao.PostDraftPhotoRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageDeleter {

	private final PostDraftPhotoRepository postDraftPhotoRepository;

	public void deleteAll(List<String> imageUrls){
		postDraftPhotoRepository.deleteByPhotoUrlIn(imageUrls);
	}
}
