package com.cotato.kampus.domain.board.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.admin.dto.BoardDetail;
import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.post.dao.PostRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFinder {
	private final BoardRepository boardRepository;
	private final PostRepository postRepository;

	public List<BoardDetail> findAllBoards() {
		// 전체 게시판 조회
		List<BoardDto> boards = boardRepository.findAll().stream()
			.map(BoardDto::from)
			.toList();

		// 게시판 별 게시글 수 매핑
		return boards.stream()
			.map(board -> {
				Long postCount = postRepository.countByBoardId(board.boardId());
				return BoardDetail.of(board, postCount);
			}).toList();
	}

	public List<BoardWithFavoriteStatusDto> findPublicBoards(){
		return boardRepository.findAllByUniversityIdIsNull().stream()
			.map(board -> BoardWithFavoriteStatusDto.of(
				board.getId(),
				board.getBoardName(),
				false
			))
			.toList();
	}

	public Board findBoard(Long boardId){
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board;
	}

	public BoardDto findBoardDto(Long boardId){
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return BoardDto.from(board);
	}

	public BoardDto findUserUniversityBoard(Long userUniversityId){
		Board board = boardRepository.findByUniversityId(userUniversityId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.from(board);
	}

	public Long findCardNewsBoardId(){
		Board board = boardRepository.findByBoardType(BoardType.CARDNEWS)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board.getId();
	}

	public List<Long> findExpiredBoardIds(LocalDateTime now){
		List<Board> expiredBoards = boardRepository.findByDeletionScheduledAtBefore(now);

		return expiredBoards.stream().map(Board::getId).toList();
	}

}