package com.cotato.kampus.domain.board.implement.boardCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategoryResolver {

	private final BoardCategoryFinder boardCategoryFinder;

	public List<Long> resolveCategoryIds(List<String> categoryNames, Long boardId) {
		List<BoardCategory> usableCategories = boardCategoryFinder.findAllByBoardId(boardId);

		Map<String, Long> usableCategoryIds = usableCategories.stream()
			.collect(Collectors.toMap(
				BoardCategory::getCategoryName,
				BoardCategory::getId
			));

		// 유효하지 않은 카테고리 필터링
		List<String> invalidCategories = categoryNames.stream()
			.filter(name -> !usableCategoryIds.containsKey(name))
			.collect(Collectors.toList());

		// 유효하지 않은 카테고리가 있으면 예외 발생
		if (!invalidCategories.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_CATEGORY);
		}

		return categoryNames.stream()
			.map(usableCategoryIds::get)
			.toList();
	}
}
