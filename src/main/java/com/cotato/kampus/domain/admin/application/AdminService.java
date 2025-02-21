package com.cotato.kampus.domain.admin.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.admin.dto.AdminBoardDetail;
import com.cotato.kampus.domain.admin.dto.AdminUserInfo;
import com.cotato.kampus.domain.admin.dto.StudentVerification;
import com.cotato.kampus.domain.admin.dto.VerificationPhotoDto;
import com.cotato.kampus.domain.admin.dto.VerificationWithPhoto;
import com.cotato.kampus.domain.admin.dto.response.AdminCardNewsPreview;
import com.cotato.kampus.domain.admin.dto.response.BoardInfo;
import com.cotato.kampus.domain.board.application.BoardAppender;
import com.cotato.kampus.domain.board.application.BoardDtoEnhancer;
import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.application.BoardUpdater;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.application.PostAppender;
import com.cotato.kampus.domain.post.application.PostDeleter;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.post.application.PostImageAppender;
import com.cotato.kampus.domain.post.application.PostImageDeleter;
import com.cotato.kampus.domain.post.application.PostImageFinder;
import com.cotato.kampus.domain.post.application.PostUpdater;
import com.cotato.kampus.domain.post.application.PostValidator;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
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
	private final BoardDtoEnhancer boardDtoEnhancer;
	private final VerificationRecordFinder verificationRecordFinder;
	private final VerificationRecordUpdater verificationRecordUpdater;
	private final VerificationPhotoFinder verificationPhotoFinder;
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
	private final PostImageFinder postImageFinder;
	private final PostImageDeleter postImageDeleter;
	private final PostValidator postValidator;

	@Transactional
	public Long createBoard(String boardName, String description, String universityName, Boolean isCategoryRequired) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 이름 중복 검사
		boardValidator.validateUniqueName(boardName);

		// 학교 게시판인 경우
		if (universityName != null) {
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

	public List<AdminBoardDetail> getBoards(BoardStatus boardStatus) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 각 게시판의 게시글 수 매핑하여 반환
		List<BoardDto> boardDtos = boardFinder.findAllBoards(boardStatus);

		// 게시판 게시글 수, 삭제까지 남은 날짜 수 매핑
		return boardDtoEnhancer.mapToAdminBoardDetail(boardDtos);
	}

	public BoardInfo getBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 조회
		BoardDto boardDto = boardFinder.findBoardDto(boardId);

		// 대학 이름 조회
		if (boardDto.boardType().equals(BoardType.UNIVERSITY)) {
			String universityName = univFinder.findUniversityName(boardDto.universityId());
			return BoardInfo.from(boardDto, universityName);
		}

		return BoardInfo.from(boardDto, null);
	}

	public Slice<StudentVerification> getVerifications(int page) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		return verificationRecordFinder.findAll(page);
	}

	public VerificationWithPhoto getVerification(Long verificationRecordId) {
		userValidator.validateAdminAccess();
		VerificationRecordDto verificationRecordDto = verificationRecordFinder.findDto(verificationRecordId);
		String universityName = univFinder.findUniversityName(verificationRecordDto.universityId());
		VerificationPhotoDto verificationPhotoDto = verificationPhotoFinder.findByRecordId(verificationRecordId);
		return VerificationWithPhoto.of(verificationRecordDto, universityName, verificationPhotoDto);
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
	public void rejectStudentVerification(Long verificationRecordId, String rejectionReason) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		verificationRecordUpdater.reject(verificationRecordId, rejectionReason);
	}

	@Transactional
	public void createCardNews(String title, String content, List<MultipartFile> images) throws ImageException {
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
		Long postId = postAppender.appendCardNews(userId, boardId, title, content);

		// 카드뉴스 사진 추가
		postImageAppender.appendAll(postId, imageUrls);
	}

	@Transactional
	public void deleteCardNews(Long postId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 카드뉴스 검증
		postValidator.validateDeleteCardNews(postId);

		// 이미지 조회
		List<String> imageUrls = postImageFinder.findPostPhotos(postId);

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostPhoto 삭제
		postImageDeleter.deletePostPhotos(postId);

		// 게시글 삭제
		postDeleter.delete(postId);
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

	// 관리자 정보 조회
	public AdminUserInfo getAdminUserDetails() {
		UserDto userDto = apiUserResolver.getCurrentUserDto();
		userValidator.validateAdminAccess(userDto);
		return AdminUserInfo.from(userDto);
	}

	@Transactional
	public void changeUserRole(Long userId, UserRole role) {
		// 관리자 검증
		userValidator.validateAdminAccess();
		// 유저 권한 변경
		userUpdater.updateRole(userId, role);
	}
}