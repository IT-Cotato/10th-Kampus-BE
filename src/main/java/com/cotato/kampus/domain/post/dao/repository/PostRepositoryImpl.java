package com.cotato.kampus.domain.post.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.post.dao.entity.PostEntity;
import com.cotato.kampus.domain.post.dao.factory.PostFactory;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.enums.PostStatus;
import com.cotato.kampus.domain.post.implement.port.PostRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

	private final PostJpaRepository postJpaRepository;
	private final PostFactory postFactory;

	@Override
	public Post save(Post post){
		PostEntity entity = postFactory.createEntity(post);
		return postJpaRepository.save(entity).toDomain();
	}

	@Override
	public Optional<Post> findById(Long postId){
		return postJpaRepository.findById(postId).map(PostEntity::toDomain);
	}

	@Override
	public void delete(Post post) {
		PostEntity entity = postFactory.createEntity(post);
		postJpaRepository.delete(entity);
	}

	@Override
	public Long countByBoardId(Long boardId) {
		return postJpaRepository.countByBoardId(boardId);
	}

	@Override
	public Slice<Post> findAllByBoardIdAndIdIn(Long boardId, List<Long> postIds, Pageable pageable) {
		return postJpaRepository.findAllByIdInOrderByCreatedTimeDesc(postIds, pageable)
			.map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> searchAll(String keyword, Pageable pageable) {
		return postJpaRepository.searchAll(keyword, pageable).map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> searchAllByBoardId(String keyword, Long boardId, Pageable pageable) {
		return postJpaRepository.searchAllByBoardId(keyword, boardId, pageable).map(PostEntity::toDomain);
	}

	@Override
	public void deleteAllByBoardIdIn(List<Long> boardId) {
		postJpaRepository.deleteAllByBoardIdIn(boardId);
	}

	@Override
	public int updateStatusByBoardIdAndCurrentStatus(Long boardId, PostStatus currentStatus, PostStatus newStatus) {
		return postJpaRepository.updateStatusByBoardIdAndCurrentStatus(boardId, currentStatus, newStatus);
	}

	@Override
	public List<Post> findTopAccessiblePostsByIds(List<Long> postIds, Long userUnivId, int limit) {
		return postJpaRepository.findTopAccessiblePostsByIds(postIds, userUnivId, limit).stream()
			.map(PostEntity::toDomain)
			.toList();
	}

	@Override
	public Slice<Post> findAllAccessiblePostsByIds(List<Long> postIds, Long userUnivId, Pageable pageable) {
		return postJpaRepository.findAllAccessiblePostsByIds(postIds, userUnivId, pageable).map(PostEntity::toDomain);
	}

	@Override
	public Optional<Post> findTopByBoardIdOrderByCreatedTimeDesc(Long boardId) {
		return postJpaRepository.findTopByBoardIdOrderByCreatedTimeDesc(boardId).map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> findPostsByUserComments(Long userId, Pageable pageable) {
		return postJpaRepository.findPostsByUserComments(userId, pageable).map(PostEntity::toDomain);
	}
}
