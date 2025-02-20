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

	@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdTime DESC")
	Slice<Post> searchAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE (p.boardId = :boardId) AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdTime DESC")
	Slice<Post> searchAllByBoardId(@Param("keyword") String keyword, @Param("boardId") Long boardId, Pageable pageable);

	@Query(value = "SELECT * From Post p WHERE p.post_id IN (:postIds) ORDER BY FIELD(p.post_id, :orderList) DESC", nativeQuery = true)
	Slice<Post> findPostsByIdsInOrder(@Param("postIds") List<Long> postIds, @Param("orderList") String orderList,
		Pageable pageable);

	void deleteAllByBoardIdIn(List<Long> boardId);

	List<Post> findAllByBoardId(Long boardId);

	@Query("""
			SELECT p FROM Post p
			JOIN Board b ON p.boardId = b.id
			WHERE p.id IN :postIds
			AND (b.boardType <> 'UNIVERSITY' OR b.universityId = :userUnivId)
			ORDER BY p.createdTime DESC
			LIMIT 5		
		""")
	List<Post> findTop5TrendingPosts(@Param("postIds") List<Long> postIds, @Param("userUnivId") Long userUnivId);

	@Query("""
				SELECT p FROM Post p
				JOIN Board b ON p.boardId = b.id
				WHERE p.id IN :postIds
				AND b.boardStatus = 'ACTIVE'
				AND (b.boardType <> 'UNIVERSITY' OR b.universityId = :userUnivId)
				ORDER BY p.createdTime DESC
		""")
	Slice<Post> findTrendingPosts(@Param("postIds") List<Long> postIds, @Param("userUnivId") Long userUnivId,
		Pageable pageable);
}