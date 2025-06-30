package com.cotato.kampus.domain.post.implement.port;

import java.util.List;

import com.cotato.kampus.domain.post.domain.TemporaryPostCategory;

public interface TemporaryPostCategoryRepository {

	void saveAll(List<TemporaryPostCategory> temporaryPostCategories);

	void deleteAllByTemporaryPostIdIn(List<Long> tempPostIds);

	void deleteAllByTemporaryPostId(Long tempPostId);

	List<TemporaryPostCategory> findAllByTemporaryPostId(Long tempPostId);
}
