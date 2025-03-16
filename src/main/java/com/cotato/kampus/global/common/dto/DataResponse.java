package com.cotato.kampus.global.common.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T> extends BaseResponse {

	private final T data;

	private DataResponse(HttpStatus status, T data) {
		super(status);
		this.data = data;
	}

	public static <T> DataResponse<T> from(T data) {
		return new DataResponse<>(HttpStatus.OK, data);
	}

	public static <T> DataResponse<Void> ok() {
		return new DataResponse<>(HttpStatus.OK, null);
	}

	// 클라이언트 오류(400)로 실패 응답생성.
	public static <T> DataResponse<T> fail(T data) {
		return new DataResponse<>(HttpStatus.BAD_REQUEST, data);
	}

	// 지정된 HTTP 상태로 실패 응답생성.
	public static <T> DataResponse<T> fail(HttpStatus status, T data) {
		return new DataResponse<>(status, data);
	}


}
