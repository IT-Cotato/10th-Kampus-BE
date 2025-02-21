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
	POST_UNLIKE_FORBIDDEN(HttpStatus.FORBIDDEN, "좋아요 내역이 없는 게시글입니다.", "POST-003"),
	POST_LIKE_DUPLICATED(HttpStatus.FORBIDDEN, "이미 좋아요한 게시글입니다.", "POST-004"),
	POST_SCRAP_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 게시글을 스크랩 할 수 없습니다.", "POST-005"),
	POST_SCRAP_DUPLICATED(HttpStatus.FORBIDDEN, "이미 스크랩한 글입니다.", "POST-006"),
	POST_SCRAP_NOT_EXIST(HttpStatus.FORBIDDEN, "스크랩 되지 않은 게시글은 삭제할 수 없습니다.", "POST-007"),
	CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST, "이 게시판에서는 카테고리가 필수입니다.", "POST-008"),
	CATEGORY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "이 게시판에서는 카테고리를 입력할 수 없습니다.", "POST-009"),
	HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글 검색 기록이 존재하지 않습니다.", "POST-010"),
	HISTORY_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 검색 기록만 삭제할 수 있습니다.", "POST-011"),
	CARD_NEWS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 게시글은 카드뉴스 게시글이 아닙니다.", "POST-012"),

	//Image
	INVALID_DELETED_IMAGE(HttpStatus.BAD_REQUEST, "삭제 요청한 이미지 URL이 유효하지 않습니다.", "IMAGE-001"),
	IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미지는 필수입니다. 이미지를 첨부해주세요.", "IMAGE-002"),

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
	USER_ALREADY_VERIFIED(HttpStatus.NOT_FOUND, "이미 재학생 인증된 유저입니다.", "USER-007"),
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

	// Code
	INVALID_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다.", "CODE-001"),

	//University
	UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대학을 찾을 수 없습니다.", "UNIVERSITY-001"),

	//JWT
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "헤더에 Access Token을 찾을 수 없습니다.", "JWT-001"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다.", "JWT-002"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "올바른 형식의 토큰이 아닙니다.", "JWT-003"),
	MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "토큰 내부에 공백이 있습니다.", "JWT-004"),

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
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
