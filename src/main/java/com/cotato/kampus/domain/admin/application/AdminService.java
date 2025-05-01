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
import com.cotato.kampus.domain.admin.dto.response.AdminCardNewsThumbnail;
import com.cotato.kampus.domain.admin.dto.BoardDetails;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.implement.board.BoardAppender;
import com.cotato.kampus.domain.board.implement.board.BoardDtoEnhancer;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.board.BoardUpdater;
import com.cotato.kampus.domain.board.implement.board.BoardValidator;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryAppender;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryFinder;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.implement.post.PostAppender;
import com.cotato.kampus.domain.post.implement.post.PostDeleter;
import com.cotato.kampus.domain.post.implement.post.PostDtoMapper;
import com.cotato.kampus.domain.post.implement.post.PostFinder;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoAppender;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoDeleter;
import com.cotato.kampus.domain.post.implement.postImage.PostPhotoFinder;
import com.cotato.kampus.domain.post.implement.post.PostUpdater;
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
	private final PostPhotoAppender postPhotoAppender;
	private final PostDeleter postDeleter;
	private final PostUpdater postUpdater;
	private final PostFinder postFinder;
	private final PostPhotoFinder postPhotoFinder;
	private final PostPhotoDeleter postPhotoDeleter;
	private final BoardCategoryAppender boardCategoryAppender;
	private final PostDtoMapper postDtoMapper;
	private final CategoryFinder categoryFinder;
	private final BoardCategoryFinder boardCategoryFinder;

	@Transactional
	public Long createBoard(String boardName, String description, BoardType boardType, String universityCode, List<String> categoryNames) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 이름 중복 검사
		boardValidator.validateUniqueName(boardName);

		Long universityId = null;
		if (universityCode != null) {
			universityId = univFinder.findIdByCode(universityCode);
			boardValidator.validateUniversityBoardExists(universityId);
		}

		boolean usesCategories = !categoryNames.isEmpty();
		Long boardId = boardAppender.appendBoard(boardName, description, boardType, universityId, usesCategories).getId();

		// 카테고리 추가 로직
		List<Long> categoryIds = categoryNames.stream().map(name -> categoryFinder.find(name).getId()).toList();
		boardCategoryAppender.appendCategories(boardId, categoryIds);

		return boardId;
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
		postUpdater.revertPendingAllByBoardId(boardId);
	}

	@Transactional
	public void pendingBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 삭제 대기 상태로 변경
		boardUpdater.pendingBoard(boardId);

		// 포함된 게시글 상태 변경
		postUpdater.pendingAllByBoardId(boardId);
	}

	@Scheduled(cron = "0 0 3 * * *")
	public void deleteExpiredBoards() {
		// 삭제할 게시판 조회
		List<Long> expiredBoardIds = boardFinder.findExpiredBoardIds(LocalDateTime.now());

		// 포함된 게시글 삭제
		postDeleter.deleteAllByBoardIds(expiredBoardIds);

		// 게시판 삭제
		boardUpdater.deleteExpiredBoards();
	}

	public List<AdminBoardDetail> getBoards(BoardStatus boardStatus) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 각 게시판의 게시글 수 매핑하여 반환
		List<Board> boards = boardFinder.findAllBoards(boardStatus);

		// 게시판 게시글 수, 삭제까지 남은 날짜 수 매핑
		return boardDtoEnhancer.mapToAdminBoardDetail(boards);
	}

	public BoardDetails getBoard(Long boardId) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 조회
		Board board = boardFinder.findBoard(boardId);

		List<Long> categoryIds = boardCategoryFinder.findAllByBoardId(boardId);
		List<Category> categories = categoryIds.stream().map(categoryFinder::find).toList();

		// 대학 이름 조회
		if (board instanceof UniversityBoard) {
			String universityName = univFinder.findUniversityName(((UniversityBoard) board).getUniversityId());
			return BoardDetails.from(board, universityName, categories);
		}

		return BoardDetails.from(board, null, categories);
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
		Long postId = postAppender.appendCardNewsPost(userId, boardId, title, content).getId();

		// 카드뉴스 사진 추가
		postPhotoAppender.appendAll(postId, imageUrls);
	}

	@Transactional
	public void deleteCardNews(Long postId) {
		// 관리자 검증
		Post post = postFinder.find(postId);
		userValidator.validateAdminAccess();

		// 이미지 조회
		List<PostPhoto> postPhotos = postPhotoFinder.findPostPhotos(postId);
		List<String> imageUrls = postPhotos.stream().map(PostPhoto::getPhotoUrl).toList();

		// S3에서 이미지 삭제
		s3Uploader.deleteFiles(imageUrls);

		// PostPhoto 삭제
		postPhotoDeleter.deletePostPhotos(postId);

		// 게시글 삭제
		postDeleter.delete(post);
	}

	public Slice<AdminCardNewsThumbnail> getAllCardNews(int page) {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 카드뉴스 게시판 조회
		Long cardNewsBoardId = boardFinder.findCardNewsBoardId();

		// 카드뉴스 조회
		Slice<Post> posts = postFinder.findAllByBoardId(cardNewsBoardId, page, PostSortType.recent);

		return postDtoMapper.toAdminCardNewsThumbnails(posts);
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