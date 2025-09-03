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
	public Post save(Post post) {
		PostEntity entity = postFactory.createEntity(post);
		return postJpaRepository.save(entity).toDomain();
	}

	@Override
	public Optional<Post> findById(Long postId) {
		return postJpaRepository.findById(postId).map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> findAllByBoardIdAndPostStatus(Long boardId, PostStatus postStatus, Pageable pageable) {
		return postJpaRepository.findAllByBoardIdAndPostStatus(boardId, postStatus, pageable)
			.map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> findAllByIdInAndPostStatus(List<Long> postIds, PostStatus postStatus, Pageable pageable) {
		return postJpaRepository.findAllByIdInAndPostStatus(postIds, postStatus, pageable)
			.map(entity -> postFactory.createDomain(entity));
	}

	@Override
	public Slice<Post> findAllByUserIdAndPostStatus(Long userId, PostStatus postStatus, Pageable pageable) {
		return postJpaRepository.findAllByUserIdAndPostStatus(userId, postStatus, pageable)
			.map(PostEntity::toDomain);
	}

	@Override
	public void delete(Post post) {
		PostEntity entity = postFactory.createEntity(post);
		postJpaRepository.delete(entity);
	}

	@Override
	public Slice<Post> findAllByBoardIdAndIdInAndPostStatus(Long boardId, List<Long> postIds, PostStatus postStatus, Pageable pageable) {
		return postJpaRepository.findAllByBoardIdAndIdInAndPostStatus(boardId, postIds, postStatus, pageable)
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
	public Slice<Post> findTopAccessiblePostsByIds(List<Long> postIds, Long userUnivId, Pageable pageable) {
		return postJpaRepository.findTopAccessiblePostsByIds(postIds, userUnivId, pageable)
			.map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> findAllAccessiblePostsByIds(List<Long> postIds, Long userUnivId, Pageable pageable) {
		return postJpaRepository.findAllAccessiblePostsByIds(postIds, userUnivId, pageable).map(PostEntity::toDomain);
	}

	@Override
	public List<Post> findLatestPostPerBoard(List<Long> boardIds, String postStatus) {
		return postJpaRepository.findLatestPostPerBoard(boardIds, postStatus).stream()
			.map(PostEntity::toDomain)
			.toList();
	}

	@Override
	public Slice<Post> findByBoardIdAndPostStatusOrderByCreatedTimeDesc(Long boardId, PostStatus postStatus,
		Pageable pageable) {
		return postJpaRepository.findByBoardIdAndPostStatusOrderByCreatedTimeDesc(boardId, postStatus, pageable)
			.map(PostEntity::toDomain);
	}

	@Override
	public Slice<Post> findPostsByUserComments(Long userId, Pageable pageable) {
		return postJpaRepository.findPostsByUserComments(userId, pageable).map(PostEntity::toDomain);
	}
}
