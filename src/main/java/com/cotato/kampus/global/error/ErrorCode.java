package com.cotato.kampus.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	//400
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘 못 되었습니다.", "COMMON-002"),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 엔티티를 찾을 수 없습니다.", "COMMON-003"),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-002"),

	//Post
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.", "POST-001"),
	POST_NOT_AUTHOR(HttpStatus.FORBIDDEN, "게시글 작성자가 아닙니다.", "POST-002"),
	POST_LIKE_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 게시글을 좋아요 할 수 없습니다.", "POST-003"),
	POST_LIKE_DUPLICATED(HttpStatus.FORBIDDEN, "이미 좋아요한 게시글입니다.", "POST-004"),
	POST_SCRAP_FORBIDDEN(HttpStatus.FORBIDDEN, "자신의 게시글을 스크랩 할 수 없습니다.", "POST-005"),
	POST_SCRAP_DUPLICATED(HttpStatus.FORBIDDEN, "이미 스크랩한 글입니다.", "POST-006"),
	POST_SCRAP_NOT_EXIST(HttpStatus.FORBIDDEN, "스크랩 되지 않은 게시글은 삭제할 수 없습니다.", "POST-007"),

	//Comment
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.", "COMMENT-001"),
	INVALID_PARENT_COMMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 부모 댓글입니다.", "COMMENT-002"),
	COMMENT_NOT_AUTHOR(HttpStatus.FORBIDDEN, "댓글 작성자가 아니므로 삭제할 수 없습니다.", "COMMENT-002"),
	INVALID_COMMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 댓글입니다.", "COMMENT-004"),
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 댓글입니다.", "COMMENT-005"),

	//File
	FILE_EXTENSION_FAULT(HttpStatus.BAD_REQUEST, "F-001", "해당 파일 확장자 명이 존재하지 않습니다."),
	FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "F-002", "파일이 비어있습니다"),

	//S3 에러
	EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "S3-001", "파일이 비어 있습니다."),
	IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S3-002", "이미지 업로드 중 IO 예외 발생"),
	NO_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S3-003", "파일 확장자가 없습니다."),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S3-004", "유효하지 않은 파일 확장자입니다."),

	//User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.", "USER-001"),
	USER_UNVERIFIED(HttpStatus.NOT_FOUND, "학생 인증되지 않은 유저입니다.", "USER-002"),

	//Board
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시판을 찾을 수 없습니다.", "BOARD-001"),
	BOARD_ALREADY_FAVORITED(HttpStatus.CONFLICT, "이미 즐겨찾기에 추가된 게시판입니다.", "BOARD-002"),
	BOARD_FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기 목록에 없는 게시판입니다.", "BOARD-003"),

	//University
	UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대학을 찾을 수 없습니다.", "UNIVERSITY-001"),

	//JWT
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "헤더에 Access Token을 찾을 수 없습니다.", "JWT-001"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다.", "JWT-002"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "올바른 형식의 토큰이 아닙니다.", "JWT-003"),
	MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "토큰 내부에 공백이 있습니다.", "JWT-004"),

	// DeepL
	INVALID_DEEPL_AUTH_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "DeepL 인증키가 유효하지 않습니다.", "DEEPL-001"),
	INVALID_DEEPL_CONTENT(HttpStatus.BAD_REQUEST, "번역 요청 내용이 올바르지 않습니다.", "DEEPL-002");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
