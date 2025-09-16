package com.cotato.kampus.global.error.handler;

import java.security.SignatureException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ChatRoomDuplicatedException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.error.exception.ImageValidationException;
import com.cotato.kampus.global.error.exception.UnivCertException;
import com.cotato.kampus.global.error.response.ChatRoomDuplicatedResponse;
import com.cotato.kampus.global.error.response.ErrorResponse;
import com.deepl.api.DeepLException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 처리되지 않은 모든 예외를 잡는 핸들러
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException(Exception e, HttpServletRequest request) {
		log.error("처리되지 않은 예외 발생: ", e);
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(
			ErrorCode.INTERNAL_SERVER_ERROR,
			request
		);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleAppCustomException(AppException e, HttpServletRequest request) {
		log.error("AppException 발생: {}", e.getErrorCode().getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e,
		HttpServletRequest request) {
		log.error("Entity Not Found Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND, request);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(DeepLException.class)
	public ResponseEntity<ErrorResponse> handleDeepLException(DeepLException e,
		HttpServletRequest request) {
		log.error("DeepL Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_DEEPL_AUTH_KEY, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}

	@ExceptionHandler(UnivCertException.class)
	public ResponseEntity<ErrorResponse> handleUnivCertException(UnivCertException e, HttpServletRequest request) {
		log.error("UnivCert Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(request, e.getErrorCode(), e.getMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	// 요청 바디에 잘못된 문자가 포함되어 있는 경우 발생(ex: 한글 지웠을 때)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException e,
		HttpServletRequest request) {
		log.error("HttpMessageNotReadable Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	// Dto 내부에서 validation되는 경우 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		String errorMessage = e.getBindingResult().getFieldErrors().stream()
			.findFirst()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.orElse("잘못된 요청입니다.");

		ErrorResponse errorResponse = ErrorResponse.of(request, ErrorCode.INVALID_PARAMETER, errorMessage);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException e,
		HttpServletRequest request) {

		// 파라미터 유효성 검증 결과에서 오류 메시지 추출
		String errorMessage = e.getParameterValidationResults().stream()
			.flatMap(result -> result.getResolvableErrors().stream())
			.map(error -> error.getDefaultMessage())
			.findFirst()
			.orElse("잘못된 요청입니다.");

		ErrorResponse errorResponse = ErrorResponse.of(request, ErrorCode.INVALID_PARAMETER, errorMessage);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// @RequestParam에서 validation되는 경우 처리
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException e,
		HttpServletRequest request) {
		log.error("ConstraintViolation Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		String errorMessage = e.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.findFirst()
			.orElse("잘못된 요청입니다.");
		ErrorResponse errorResponse = ErrorResponse.of(request, ErrorCode.INVALID_PARAMETER, errorMessage);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<ErrorResponse> handleImageException(ImageException e,
		HttpServletRequest request) {
		log.error("Image Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
		MissingServletRequestPartException e, HttpServletRequest request) {
		log.error("파일 누락 예외: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.FILE_IS_EMPTY, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	@ExceptionHandler(ImageValidationException.class)
	public ResponseEntity<ErrorResponse> handleImageValidationException(ImageValidationException e,
		HttpServletRequest request) {
		log.error("Image Validation Exception 발생: {}", e.getDetailMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(request, e.getErrorCode(), e.getDetailMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	// JWT 관련 예외 처리
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException e,
		HttpServletRequest request) {
		log.error("잘못된 형식의 JWT 토큰: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MALFORMED_TOKEN, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request) {
		log.error("만료된 JWT 토큰: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_EXPIRED, request);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e, HttpServletRequest request) {
		log.error("JWT 서명 검증 실패: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MALFORMED_TOKEN, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ChatRoomDuplicatedException.class)
	public ResponseEntity<ChatRoomDuplicatedResponse> handleChatRoomDuplicatedException(ChatRoomDuplicatedException e, HttpServletRequest request) {
		log.error("ChatRoom Duplicated Exception 발생: {}, 기존 채팅방 ID: {}", e.getMessage(), e.getExistingChatRoomId());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());

		ChatRoomDuplicatedResponse errorResponse = ChatRoomDuplicatedResponse.of(
			e.getErrorCode(),
			request,
			e.getExistingChatRoomId()
		);

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}
}
