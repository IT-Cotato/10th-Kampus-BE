package com.cotato.kampus.domain.board.implement.board;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.AdminBoardDetail;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.BoardWithPostCount;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteFinder;
import com.cotato.kampus.domain.board.domain.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDtoMapper {

	private final PostFinder postFinder;
	private final BoardFavoriteFinder boardFavoriteFinder;

	public BoardWithFavoriteStatus mapToBoardWithFavoriteStatus(Board board, UserDto userDto) {
		Boolean isFavorite = boardFavoriteFinder.existsByUserIdAndBoardId(userDto.id(), board.getId());

		return BoardWithFavoriteStatus.from(board, isFavorite);
	}

	public List<BoardWithFavoriteStatus> updateFavoriteStatus(List<Board> boards, Set<Long> favoriteBoardIds) {
		return boards.stream()
			.map(board -> BoardWithFavoriteStatus.from(board, favoriteBoardIds.contains(board.getId())
			))
			.toList();
	}

	public List<AdminBoardDetail> mapToAdminBoardDetail(List<BoardWithPostCount> boardsWithPostCount) {
		LocalDateTime now = LocalDateTime.now();

		return boardsWithPostCount.stream()
			.map(boardWithPostCount -> {
				Board board = boardWithPostCount.board();
				Long postCount = boardWithPostCount.postCount();

				// 삭제 대기인 게시판은 삭제 날짜 카운트 반환
				if (board.getBoardStatus().equals(BoardStatus.PENDING_DELETION)) {
					// 삭제까지 남은 날짜
					Long deletionCountDown = ChronoUnit.DAYS.between(now, board.getDeletionScheduledAt());
					return AdminBoardDetail.of(board, postCount, deletionCountDown);
				}

				return AdminBoardDetail.of(board, postCount, null);
			}).toList();
	}
}