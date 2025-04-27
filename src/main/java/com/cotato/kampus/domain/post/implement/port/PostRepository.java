package com.cotato.kampus.domain.post.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;

public interface PostRepository {

	Post save(Post post);

	Optional<Post> findById(Long postId);

	Slice<Post> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable);

	Slice<Post> findAllByIdInAndPostStatus(List<Long> postIds, PostStatus postStatus, Pageable pageable);

	Slice<Post> findAllByUserIdAndPostStatus(Long userId, PostStatus postStatus, Pageable pageable);

	void delete(Post post);

	// 게시판의 총 게시글 수
	Long countByBoardId(Long boardId);

	// 게시판에서 일부(postIds) 게시글 조회
	Slice<Post> findAllByBoardIdAndIdInAndPostStatus(Long boardId, List<Long> postIds, PostStatus postStatus, Pageable pageable);

	// 전체 게시글 검색
	Slice<Post> searchAll(String keyword, Pageable pageable);

	// 게시판 내 게시글 검색
	Slice<Post> searchAllByBoardId(String keyword, Long boardId, Pageable pageable);

	// 게시판 내 게시글 전체 삭제
	void deleteAllByBoardIdIn(List<Long> boardId);

	// 게시판 내 게시글 상태 변경 (게시판 상태 변경 시 사용)
	int updateStatusByBoardIdAndCurrentStatus(Long boardId, PostStatus currentStatus, PostStatus newStatus);

	// 접근 가능한 게시판의 게시글 중 지정된 ID 목록에 해당하는 상위 N개 조회
	List<Post> findTopAccessiblePostsByIds(List<Long> postIds, Long userUnivId, int limit);

	// 접근 가능한 게시글 목록 페이징 조회
	Slice<Post> findAllAccessiblePostsByIds(List<Long> postIds, Long userUnivId,
		Pageable pageable);

	// 게시판의 최신 게시글 1개 조회
	Post findTopByBoardIdOrderByCreatedTimeDesc(Long boardId);

	// 사용자가 댓글을 작성한 게시글 목록 조회
	Slice<Post> findPostsByUserComments(Long userId, Pageable pageable);
}