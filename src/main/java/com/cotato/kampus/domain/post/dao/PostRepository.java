package com.cotato.kampus.domain.post.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Slice<Post> findAllByUserId(Long userId, Pageable pageable);

	Long countByBoardId(Long boardId);

	Slice<Post> findAllByBoardIdOrderByCreatedTimeDesc(Long boardId, Pageable pageable);

	Slice<Post> findAllByBoardIdOrderByCreatedTimeAsc(Long boardId, Pageable pageable);

	Slice<Post> findAllByBoardIdOrderByLikesDescCreatedTimeDesc(Long boardId, Pageable pageable);

	@Query(value = """
		SELECT * From Post p
		WHERE p.post_id IN (:postIds)
		ORDER BY FIELD(p.post_id, :orderList) DESC
	""", nativeQuery = true)
	Slice<Post> findPostsByIdsInOrder(
		@Param("postIds") List<Long> postIds,
		@Param("orderList") String orderList,
		Pageable pageable);
}