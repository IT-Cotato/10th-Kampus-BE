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

	//Board
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시판을 찾을 수 없습니다.", "BOARD-001"),
	BOARD_ALREADY_FAVORITED(HttpStatus.CONFLICT, "이미 즐겨찾기에 추가된 게시판입니다.", "BOARD-002");

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
