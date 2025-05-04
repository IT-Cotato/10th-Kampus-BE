package com.cotato.kampus.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	//400
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘못 되었습니다.", "COMMON-002"),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다.", "COMMON-003"),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-002"),

	//Post
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.", "POST-001"),
	POST_NOT_AUTHOR(HttpStatus.FORBIDDEN, "게시글 작성자가 아닙니다.", "POST-002"),
	POST_SCRAP_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 게시글을 스크랩 할 수 없습니다.", "POST-005"),
	POST_SCRAP_DUPLICATED(HttpStatus.FORBIDDEN, "이미 스크랩한 글입니다.", "POST-006"),
	POST_SCRAP_NOT_EXIST(HttpStatus.FORBIDDEN, "스크랩 되지 않은 게시글은 삭제할 수 없습니다.", "POST-007"),
	INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "해당 게시판에서 사용할 수 없는 카테고리입니다.", "POST-008"),
	CATEGORY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "카테고리가 없는 게시판입니다.", "POST-009"),
	HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글 검색 기록이 존재하지 않습니다.", "POST-010"),
	HISTORY_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 검색 기록만 삭제할 수 있습니다.", "POST-011"),
	CARD_NEWS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 게시글은 카드뉴스 게시글이 아닙니다.", "POST-012"),
	POST_REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "게시글 필수 항목이 누락되었습니다.", "POST-013"),

	POST_BOARD_ID_REQUIRED(HttpStatus.BAD_REQUEST, "게시글 작성 시 게시판 ID는 필수입니다.", "POST-014"),
	POST_AUTHOR_ID_REQUIRED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 작성 시 작성자 ID는 필수입니다.", "POST-015"),
	POST_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "게시글 제목은 필수 항목입니다.", "POST-016"),
	POST_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "게시글 내용은 필수 항목입니다.", "POST-017"),
	POST_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "게시글 내용이 너무 깁니다", "POST-018"),
	POST_STATUS_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 상태는 필수 항목입니다.", "POST-019"),
	POST_TYPE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 타입은 필수 항목입니다.", "POST-020"),
	POST_ANONYMOUS_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 익명 여부는 필수 항목입니다.", "POST-021"),
	INVALID_POST_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 PostType 입니다.", "POST-022"),
	POST_TYPE_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 게시글 타입입니다.", "POST-023"),
	POST_NOT_PUBLISHED(HttpStatus.BAD_REQUEST, "게시 상태가 아닌 게시글에는 해당 작업을 수행할 수 없습니다.", "POST-024"),

	// TemporaryPost
	TEMP_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "임시저장 게시글을 찾을 수 없습니다.", "TEMP_POST-001"),
	TEMP_POST_NOT_AUTHOR(HttpStatus.FORBIDDEN, "임시저장 게시글 작성자가 아닙니다.", "TEMP_POST-002"),
	TEMP_POST_ID_REQUIRED(HttpStatus.BAD_REQUEST, "임시저장 게시글 ID는 필수입니다.", "TEMP_POST-003"),
	TEMP_PHOTO_URL_REQUIRED(HttpStatus.BAD_REQUEST, "임시저장 사진 URL은 필수입니다.", "TEMP_POST-004"),
	TEMP_PHOTO_ORDER_REQUIRED(HttpStatus.BAD_REQUEST, "임시저장 사진 order는 필수입니다.", "TEMP_POST-005"),

	// TrendingPost
	TRENDING_POST_ID_REQUIRED(HttpStatus.BAD_REQUEST, "TrendingPost 게시글 ID는 필수입니다.", "TRENDING_POST-001"),

	// PostLike
	POST_LIKE_USER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PostLike 사용자 ID는 필수입니다.", "POST_LIKE-001"),
	POST_LIKE_POST_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PostLike 게시글 ID는 필수입니다.", "POST_LIKE-002"),
	POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 내역을 찾을 수 없습니다. ", "POST_LIKE-003"),
	POST_LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 좋아요한 게시글입니다.", "POST_LIKE-004"),

	// PostScrap
	POST_SCRAP_USER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PostScrap 사용자 ID는 필수입니다.", "POST_SCRAP-001"),
	POST_SCRAP_POST_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PostScrap 게시글 ID는 필수입니다.", "POST_SCRAP-002"),

	// PostPhoto
	POST_PHOTO_POST_ID_REQUIRED(HttpStatus.BAD_REQUEST, "PostPhoto 게시글 ID는 필수입니다.", "POST_PHOTO-001"),
	POST_PHOTO_URL_REQUIRED(HttpStatus.BAD_REQUEST, "PostScrap 사진 URL은 필수입니다.", "POST_PHOTO-002"),

	// Category
	CATEGORY_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "Category 생성시 카테고리 이름은 필수입니다.", "CATEGORY-001"),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category를 찾을 수 없습니다.", "CATEGORY-002"),
	CATEGORY_NOT_BELONG_TO_BOARD(HttpStatus.BAD_REQUEST, "카테고리가 해당 게시판에 속하지 않습니다.", "CATEGORY-003"),
	CATEGORY_DUPLICATED(HttpStatus.BAD_REQUEST, "카테고리가 중복됩니다.", "Category-004"),

	//Image
	INVALID_DELETED_IMAGE(HttpStatus.BAD_REQUEST, "삭제 요청한 이미지 URL이 유효하지 않습니다.", "IMAGE-001"),
	IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지는 필수입니다. 이미지를 첨부해주세요.", "IMAGE-002"),
	IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "한 번에 보낼 수 있는 이미지 수를 초과했습니다.", "IMAGE-003"),

	//Comment
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.", "COMMENT-001"),
	INVALID_PARENT_COMMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 부모 댓글입니다.", "COMMENT-002"),
	COMMENT_NOT_AUTHOR(HttpStatus.FORBIDDEN, "댓글 작성자가 아니므로 삭제할 수 없습니다.", "COMMENT-002"),
	INVALID_COMMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 댓글입니다.", "COMMENT-004"),
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 댓글입니다.", "COMMENT-005"),
	COMMENT_UNLIKE_FORBIDDEN(HttpStatus.BAD_REQUEST, "댓글 좋아요 취소가 불가능합니다.", "COMMENT-006"),

	//File
	FILE_EXTENSION_FAULT(HttpStatus.BAD_REQUEST, "F-001", "해당 파일 확장자 명이 존재하지 않습니다."),
	FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "F-002", "파일이 비어있습니다"),
	FILE_SIZE_TOO_LARGE(HttpStatus.BAD_REQUEST, "F-003", "파일 크기가 너무 큽니다"),

	//S3 에러
	EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "S3-001", "파일이 비어 있습니다."),
	IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S3-002", "이미지 업로드 중 IO 예외 발생"),
	NO_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S3-003", "파일 확장자가 없습니다."),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S3-004", "유효하지 않은 파일 확장자입니다."),

	//User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.", "USER-001"),
	USER_UNVERIFIED(HttpStatus.NOT_FOUND, "재학생 인증되지 않은 유저입니다.", "USER-002"),
	USER_NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다.", "USER-003"),
	USER_NOT_ADMIN(HttpStatus.FORBIDDEN, "사용자가 관리자 권한을 갖고 있지 않습니다.", "USER-004"),
	AGREEMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 유저의 동의 내역이 이미 존재합니다.", "USER-005"),
	USER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 세부정보를 등록한 유저입니다.", "USER-006"),
	USER_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 재학생 인증된 유저입니다.", "USER-007"),
	USER_ROLE_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "같은 권한으로 변경할 수 없습니다.", "USER-008"),

	//Board
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시판을 찾을 수 없습니다.", "BOARD-001"),
	BOARD_ALREADY_FAVORITED(HttpStatus.CONFLICT, "이미 즐겨찾기에 추가된 게시판입니다.", "BOARD-002"),
	BOARD_FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기 목록에 없는 게시판입니다.", "BOARD-003"),
	UNIVERSITY_BOARD_DUPLICATED(HttpStatus.CONFLICT, "이미 해당 대학 게시판이 존재합니다.", "BOARD-004"),
	BOARD_ALREADY_INACTIVE(HttpStatus.CONFLICT, "이미 게시판이 비활성화 상태입니다.", "BOARD-005"),
	BOARD_ALREADY_ACTIVE(HttpStatus.CONFLICT, "이미 게시판이 활성화 상태입니다.", "BOARD-006"),
	BOARD_NOT_VALIDATE(HttpStatus.BAD_REQUEST, "게시판이 유효하지 않습니다.", "BOARD-007"),
	BOARD_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "게시판 이름이 이미 존재합니다.", "BOARD-008"),
	BOARD_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 게시판 접근 자격이 없습니다.", "BOARD-009"),
	BOARD_ALREADY_PENDING(HttpStatus.BAD_REQUEST, "이미 삭제 대기 상태인 게시판입니다.", "BOARD-010"),
	INVALID_BOARD_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 BoardType 입니다.", "BOARD-011"),
	BOARD_NOT_ACTIVE(HttpStatus.FORBIDDEN, "게시판이 활성화되지 않았습니다.", "BOARD-012"),

	BOARD_NAME_EMPTY(HttpStatus.BAD_REQUEST, "게시판 이름은 필수 항목입니다.", "BOARD-012"),
	BOARD_DESCRIPTION_EMPTY(HttpStatus.BAD_REQUEST, "게시판 설명은 필수 항목입니다.", "BOARD-013"),
	BOARD_DESCRIPTION_TOO_LONG(HttpStatus.BAD_REQUEST, "게시판 설명이 너무 깁니다.", "BOARD-014"),
	BOARD_STATUS_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "게시판 상태는 필수 항목입니다.", "BOARD-015"),
	BOARD_TYPE_EMPTY(HttpStatus.BAD_REQUEST, "게시판 타입은 필수 항목입니다.", "BOARD-016"),
	BOARD_UNIVERSITY_ID_REQUIRED(HttpStatus.BAD_REQUEST, "대학 게시판은 대학 ID가 필수입니다.", "BOARD-017"),

	//University
	UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대학을 찾을 수 없습니다.", "UNIVERSITY-001"),

	//JWT
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "헤더에 Access Token을 찾을 수 없습니다.", "JWT-001"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다.", "JWT-002"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "JWT-003"),
	MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "토큰 형식에 문제가 있습니다.", "JWT-004"),

	// DeepL
	INVALID_DEEPL_AUTH_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "DeepL 인증키가 유효하지 않습니다.", "DEEPL-001"),
	INVALID_DEEPL_CONTENT(HttpStatus.BAD_REQUEST, "번역 요청 내용이 올바르지 않습니다.", "DEEPL-002"),

	// Chat
	INVALID_CHATROOM(HttpStatus.FORBIDDEN, "자신에게 채팅을 할 수 없습니다.", "CHAT-001"),
	CHATROOM_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 채팅 방입니다.", "CHAT-002"),
	CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다.", "CHAT-003"),
	CHATROOM_NOT_ENTERED(HttpStatus.FORBIDDEN, "채팅방에 입장한 유저가 아닙니다.", "CHAT-004"),
	READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅 읽음 상태를 찾을 수 없습니다.", "CHAT-005"),
	CHATROOM_METADATA_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방 메타데이터를 찾을 수 없습니다.", "CHAT-006"),

	// Verification
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "재학생 인증 요청 기록을 찾을 수 없습니다.", "VERIFICATION-001"),
	VERIFICATION_PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "재학생 인증 사진을 찾을 수 없습니다.", "VERIFICATION-002"),

	// fcm
	GOOGLE_REQUEST_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "google request token error", "FCM-001"),

	// Notice
	NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공지를 찾을 수 없습니다.", "NOTICE-001"),

	// Language
	INVALID_LANGUAGE_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 언어 코드입니다.", "LANGUAGE-001"),
	INVALID_LANGUAGE_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 언어명입니다.", "LANGUAGE-002"),

	// Nationality
	INVALID_NATIONALITY_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 국가 코드입니다.", "NATIONALITY-001"),
	INVALID_NATIONALITY_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 국가명입니다.", "NATIONALITY-002"),

	// Inquiry
	INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의글을 찾을 수 없습니다.", "INQUIRY-001"),
	INQUIRY_NOT_AUTHOR(HttpStatus.FORBIDDEN, "문의글 작성자가 아닙니다.", "INQUIRY-002"),

	// UnivCert
	UNIVCERT_ERROR(HttpStatus.BAD_REQUEST, "대학 이메일 인증 중 오류가 발생하였습니다.", "UNIVCERT-001"),
	;
	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
