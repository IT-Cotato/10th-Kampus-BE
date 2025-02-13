package com.cotato.kampus.domain.notification.api;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.notification.application.FcmService;
import com.cotato.kampus.domain.notification.dto.FcmRequestDto;
import com.cotato.kampus.global.common.dto.DataResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/fcm")
public class FcmController {

	private final FcmService fcmService;

	// Client가 Server로 알림 생성 요청
	@PostMapping("/pushMessage")
	public ResponseEntity<DataResponse<Void>> pushMessage(
		@RequestBody FcmRequestDto requestDto) throws IOException
	{
		System.out.println(requestDto.deviceToken()
		+ " " + requestDto.title()
		+ " " + requestDto.body());

		fcmService.sendMessageTo(
			requestDto.deviceToken(),
			requestDto.title(),
			requestDto.body()
		);

		return ResponseEntity.ok(DataResponse.ok());
	}
}
