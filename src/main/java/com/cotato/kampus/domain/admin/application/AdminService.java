package com.cotato.kampus.domain.admin.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.admin.dto.BoardDetail;
import com.cotato.kampus.domain.admin.dto.StudentVerification;
import com.cotato.kampus.domain.admin.dto.response.AdminCardNewsPreview;
import com.cotato.kampus.domain.board.application.BoardAppender;
import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.application.BoardUpdater;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.application.PostAppender;
import com.cotato.kampus.domain.post.application.PostDeleter;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.application.PostImageAppender;
import com.cotato.kampus.domain.post.application.PostUpdater;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.verification.application.VerificationRecordFinder;
import com.cotato.kampus.domain.verification.application.VerificationRecordUpdater;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AdminService {

	private final UserValidator userValidator;
	private final BoardAppender boardAppender;
	private final BoardUpdater boardUpdater;
	private final BoardValidator boardValidator;
	private final BoardFinder boardFinder;
	private final VerificationRecordFinder verificationRecordFinder;
	private final VerificationRecordUpdater verificationRecordUpdater;
	private final UserUpdater userUpdater;
	private final ImageValidator imageValidator;
	private final S3Uploader s3Uploader;
	private final UnivFinder univFinder;

	private static final String CARDNEWS_IMAGE_FOLDER = "cardNews";
	private final ApiUserResolver apiUserResolver;
	private final PostAppender postAppender;
	private final PostImageAppender postImageAppender;
	private final PostDeleter postDeleter;
	private final PostUpdater postUpdater;
	private final PostFinder postFinder;

	@Transactional
	public Long createBoard(String boardName, String description, String universityName, Boolean isCategoryRequired) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 이름 중복 검사
		boardValidator.validateUniqueName(boardName);

		// 학교 게시판인 경우
		if(universityName != null) {
			Long universityId = univFinder.findUniversityId(universityName);
			boardValidator.validateUniversityBoardExists(universityId);

			return boardAppender.appendUniversityBoard(boardName, description, universityId, isCategoryRequired);
		}

		// 일반 게시판인 경우
		return boardAppender.appendBoard(boardName, description, isCategoryRequired);
	}

	@Transactional
	public void updateBoard(Long boardId, String boardName, String description, Boolean isCategoryRequired) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 업데이트
		boardUpdater.update(boardId, boardName, description, isCategoryRequired);
	}

	@Transactional
	public void inactiveBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 비활성화
		boardUpdater.inactiveBoard(boardId);
	}

	@Transactional
	public void activeBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 활성화
		boardUpdater.activeBoard(boardId);

		// 게시글 상태 변경
		postUpdater.revertPendingPosts(boardId);
	}

	@Transactional
	public void pendingBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 삭제 대기 상태로 변경
		boardUpdater.pendingBoard(boardId);

		// 포함된 게시글 상태 변경
		postUpdater.pendingPost(boardId);
	}

	@Scheduled(cron = "0 0 3 * * *")
	public void deleteExpiredBoards() {
		// 삭제할 게시판 조회
		List<Long> expiredBoardIds = boardFinder.findExpiredBoardIds(LocalDateTime.now());

		// 포함된 게시글 삭제
		postDeleter.deletePostsByBoardIds(expiredBoardIds);

		// 게시판 삭제
		boardUpdater.deleteExpiredBoards();
	}

	public List<BoardDetail> getBoards(BoardStatus boardStatus) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 각 게시판의 게시글 수 매핑하여 반환
		return boardFinder.findAllBoards(boardStatus);
	}

	public Slice<StudentVerification> getVerifications(int page) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		return verificationRecordFinder.findAll(page);
	}

	@Transactional
	public void approveStudentVerification(Long verificationRecordId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		verificationRecordUpdater.approve(verificationRecordId);

		VerificationRecordDto verificationRecordDto = verificationRecordFinder.findDto(verificationRecordId);

		Long userId = verificationRecordDto.userId();

		Long universityId = verificationRecordDto.universityId();

		// 유저 상태 변경, 학교 할당
		userUpdater.updateVerificationStatus(userId, universityId);
	}

	@Transactional
	public void rejectStudentVerification(Long verificationRecordId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		verificationRecordUpdater.reject(verificationRecordId);
	}

	@Transactional
	public void createCardNews(String title, List<MultipartFile> images) throws ImageException {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 유효한 이미지만 필터링, 이미지 없는 경우 예외처리
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);
		if (validImages.isEmpty()) {
			throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
		}

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, CARDNEWS_IMAGE_FOLDER);

		// 카드뉴스 추가
		Long userId = apiUserResolver.getCurrentUserId();
		Long boardId = boardFinder.findCardNewsBoardId();
		Long postId = postAppender.appendCardNews(userId, boardId, title);

		// 카드뉴스 사진 추가
		postImageAppender.appendAll(postId, imageUrls);
	}

	public Slice<AdminCardNewsPreview> getAllCardNews(int page) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 카드뉴스 게시판 조회
		Long cardNewsBoardId = boardFinder.findCardNewsBoardId();

		// 카드뉴스 조회
		Slice<PostWithPhotos> posts = postFinder.findPosts(cardNewsBoardId, page, PostSortType.recent);

		return posts.map(AdminCardNewsPreview::from);
	}
}