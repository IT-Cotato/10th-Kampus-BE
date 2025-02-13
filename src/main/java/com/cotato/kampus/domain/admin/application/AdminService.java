package com.cotato.kampus.domain.admin.application;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.admin.dto.BoardDetail;
import com.cotato.kampus.domain.admin.dto.StudentVerification;
import com.cotato.kampus.domain.board.application.BoardAppender;
import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.application.BoardUpdater;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.cardNews.application.CardNewsAppender;
import com.cotato.kampus.domain.cardNews.application.CardNewsImageAppender;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.verification.application.VerificationRecordFinder;
import com.cotato.kampus.domain.verification.application.VerificationRecordUpdater;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
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
	private final CardNewsAppender cardNewsAppender;
	private final CardNewsImageAppender cardNewsImageAppender;

	private static final String CARDNEWS_IMAGE_FOLDER = "CardNews";
	private final ApiUserResolver apiUserResolver;

	public Long createBoard(String boardName, String description, Long universityId, Boolean isCategoryRequired){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 학교 게시판인 경우 이미 존재하는지 확인
		if(universityId != null)
			boardValidator.validateUniversityBoardExists(universityId);

		return boardAppender.appendBoard(boardName, description, universityId, isCategoryRequired);
	}

	public void updateBoard(Long boardId, String boardName, String description, Boolean isCategoryRequired){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 업데이트
		boardUpdater.update(boardId, boardName, description, isCategoryRequired);
	}

	public void inactiveBoard(Long boardId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 비활성화
		boardUpdater.inactiveBoard(boardId);
	}

	public void activeBoard(Long boardId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 게시판 활성화
		boardUpdater.activeBoard(boardId);
	}

	public List<BoardDetail> getAllBoards(){
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 각 게시판의 게시글 수 매핑하여 반환
		return boardFinder.findAllBoards();
	}

	public Slice<StudentVerification> getVerifications(int page){
		// 관리자 검증
		userValidator.validateAdminAccess();

		return verificationRecordFinder.findAll(page);
	}

	public void approveStudentVerification(Long verificationRecordId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		verificationRecordUpdater.approve(verificationRecordId);

		VerificationRecordDto verificationRecordDto = verificationRecordFinder.findDto(verificationRecordId);

		Long userId = verificationRecordDto.userId();

		Long universityId = verificationRecordDto.universityId();

		// 유저 상태 변경, 학교 할당
		userUpdater.updateVerificationStatus(userId, universityId);
	}

	public void rejectStudentVerification(Long verificationRecordId){
		// 관리자 검증
		userValidator.validateAdminAccess();

		verificationRecordUpdater.reject(verificationRecordId);
	}

	public void createCardNews(String title, List<MultipartFile> images) throws ImageException {
		// 관리자 검증
		userValidator.validateAdminAccess();

		// 유효한 이미지만 필터링
		List<MultipartFile> validImages = imageValidator.filterValidImages(images);

		// s3에 이미지 업로드
		List<String> imageUrls = (validImages.isEmpty()) ?
			List.of() :
			s3Uploader.uploadFiles(validImages, CARDNEWS_IMAGE_FOLDER);

		// cardNews 추가
		Long userId = apiUserResolver.getUserId();
		Long cardNewsId = cardNewsAppender.append(userId, title);

		// cardNewsImage 추가
		cardNewsImageAppender.appendAll(cardNewsId, imageUrls);
	}
}