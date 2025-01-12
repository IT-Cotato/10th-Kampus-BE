package com.cotato.kampus.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostPhoto;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
}
