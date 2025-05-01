package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryAppender;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryFinder;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryValidator;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.user.application.UserValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardCategoryService {

	private final BoardFinder boardFinder;
	private final BoardCategoryFinder boardCategoryFinder;
	private final BoardCategoryAppender boardCategoryAppender;
	private final BoardCategoryValidator boardCategoryValidator;
	private final CategoryFinder categoryFinder;
	private final UserValidator userValidator;

	public List<Category> findCategories(Long boardId) {
		// 존재하는 게시판인지 확인
		boardFinder.findBoard(boardId);

		// 카테고리 조회
		List<Long> categoryIds = boardCategoryFinder.findAllByBoardId(boardId);

		return categoryIds.stream().map(categoryFinder::find).toList();
	}

	public void addCategory(Long boardId, List<String> categoryNames) {
		// 관리자 및 게시판 검증
		userValidator.validateAdminAccess();
		boardFinder.findBoard(boardId);

		// 새로운 카테고리 조회
		List<Long> newCategoryIds = categoryNames.stream()
			.map(name -> categoryFinder.find(name)
				.getId()).toList();

		// 기존 카테고리 조회/검증
		List<Long> originCategoryIds = boardCategoryFinder.findAllByBoardId(boardId);
		boardCategoryValidator.validateNoDuplicateCategories(newCategoryIds, originCategoryIds);

		// 게시판에 카테고리 추가
		boardCategoryAppender.appendCategories(boardId, newCategoryIds);
	}
}
