package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.post.dao.entity.PostEntity;
import com.cotato.kampus.domain.post.enums.PostStatus;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

	Long countByBoardId(Long boardId);

	Slice<PostEntity> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable);

	Slice<PostEntity> findAllByBoardIdAndIdInAndPostStatus(Long boardId, List<Long> postIds, PostStatus postStatus, Pageable pageable);

	Slice<PostEntity> findAllByIdInAndPostStatus(List<Long> postIds, PostStatus postStatus, Pageable pageable);

	Slice<PostEntity> findAllByUserIdAndPostStatus(Long userId, PostStatus postStatus, Pageable pageable);

	@Query("SELECT p FROM PostEntity p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdTime DESC")
	Slice<PostEntity> searchAll(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT p FROM PostEntity p WHERE (p.boardId = :boardId) AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createdTime DESC")
	Slice<PostEntity> searchAllByBoardId(@Param("keyword") String keyword, @Param("boardId") Long boardId,
		Pageable pageable);

	void deleteAllByBoardIdIn(List<Long> boardId);

	@Modifying
	@Query("""
			UPDATE PostEntity p
			SET p.postStatus = :newStatus
			WHERE p.boardId = :boardId
			AND p.postStatus =:currentStatus
		""")
	int updateStatusByBoardIdAndCurrentStatus(Long boardId, PostStatus currentStatus, PostStatus newStatus);

	@Query("""
			SELECT p FROM PostEntity p
			JOIN BoardEntity b ON p.boardId = b.id
			WHERE p.id IN :postIds
			AND p.postStatus = 'PUBLISHED'
			AND (b.boardType <> 'UNIVERSITY' OR b.universityId = :userUnivId)
			ORDER BY p.createdTime DESC
		""")
	Slice<PostEntity> findTopAccessiblePostsByIds(@Param("postIds") List<Long> postIds, @Param("userUnivId") Long userUnivId,
		Pageable pageable);

	@Query("""
				SELECT p FROM PostEntity p
				JOIN BoardEntity b ON p.boardId = b.id
				WHERE p.id IN :postIds
				AND p.postStatus = 'PUBLISHED'
				AND (b.boardType <> 'UNIVERSITY' OR b.universityId = :userUnivId)
				ORDER BY p.createdTime DESC
		""")
	Slice<PostEntity> findAllAccessiblePostsByIds(@Param("postIds") List<Long> postIds, @Param("userUnivId") Long userUnivId,
		Pageable pageable);

	Optional<PostEntity> findTopByBoardIdAndPostStatusOrderByCreatedTimeDesc(Long boardId, PostStatus postStatus);
	
	Slice<PostEntity> findByBoardIdAndPostStatusOrderByCreatedTimeDesc(Long boardId, PostStatus postStatus, Pageable pageable);

	@Query("""
		    SELECT DISTINCT p
		    FROM PostEntity p
		    JOIN Comment c ON p.id = c.postId
		    WHERE c.userId = :userId
			AND p.postStatus = 'PUBLISHED'
		    GROUP BY p.id
		    ORDER BY MAX(c.createdTime) DESC
		""")
	Slice<PostEntity> findPostsByUserComments(@Param("userId") Long userId, Pageable pageable);
}
