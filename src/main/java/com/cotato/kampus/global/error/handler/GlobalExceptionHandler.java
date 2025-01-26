package com.cotato.kampus.global.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.DeepLException;
import com.cotato.kampus.global.error.exception.JwtException;
import com.cotato.kampus.global.error.response.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ErrorResponse> handleJwtException(JwtException e,
		HttpServletRequest request) {
		log.error("엑세스 토큰 만료 Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	@ExceptionHandler(DeepLException.class)
	public ResponseEntity<ErrorResponse> handleDeepLException(JwtException e,
		HttpServletRequest request) {
		log.error("DeepL Exception 발생: {}", e.getMessage());
		log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}
}
