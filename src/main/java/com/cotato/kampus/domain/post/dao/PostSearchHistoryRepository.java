package com.cotato.kampus.domain.post.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.PostSearchHistory;

@Repository
public interface PostSearchHistoryRepository extends JpaRepository<PostSearchHistory, Long> {

	List<PostSearchHistory> findTop5ByUserIdOrderByCreatedTimeDesc(Long userId);

	Optional<PostSearchHistory> findByUserIdAndKeyword(Long userId, String keyword);

	List<PostSearchHistory> findByUserIdOrderByCreatedTimeDesc(Long userId);
}