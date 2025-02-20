package com.cotato.kampus.domain.board.application;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.AdminBoardDetail;
import com.cotato.kampus.domain.board.dao.BoardFavoriteRepository;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDtoEnhancer {

	private final PostRepository postRepository;
	private final BoardFavoriteRepository boardFavoriteRepository;

	public BoardWithFavoriteStatus mapToBoardWithFavoriteStatus(BoardDto boardDto, UserDto userDto) {
		Boolean isFavorite = boardFavoriteRepository.existsByUserIdAndBoardId(userDto.id(), boardDto.boardId());

		return BoardWithFavoriteStatus.from(boardDto, isFavorite);

	}

	public List<BoardWithFavoriteStatus> updateFavoriteStatus(List<BoardDto> boardDtos, List<Long> favoriteBoardIds) {
		return boardDtos.stream()
			.map(boardDto -> BoardWithFavoriteStatus.from(boardDto, favoriteBoardIds.contains(boardDto.boardId())
			))
			.toList();
	}

	public List<AdminBoardDetail> mapToAdminBoardDetail(List<BoardDto> boards) {
		LocalDateTime now = LocalDateTime.now();

		return boards.stream()
			.map(board -> {
				// 게시글 수
				Long postCount = postRepository.countByBoardId(board.boardId());

				// 삭제 대기인 게시글은 삭제 날짜 카운트 반환
				if (board.boardStatus().equals(BoardStatus.PENDING_DELETION)) {
					// 삭제까지 남은 날짜
					Long deletionCountDown = ChronoUnit.DAYS.between(now, board.deletionScheduledAt());
					return AdminBoardDetail.of(board, postCount, deletionCountDown);
				}

				return AdminBoardDetail.of(board, postCount, null);
			}).toList();
	}

}