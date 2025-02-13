package com.cotato.kampus.domain.notification.api;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.kampus.domain.notification.application.FcmService;
import com.cotato.kampus.domain.notification.dto.FcmRequestDto;
import com.cotato.kampus.global.common.dto.DataResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/v1/api/fcm")
public class FcmController {

	private final FcmService fcmService;

	// Client가 Server로 알림 생성 요청
	@PostMapping("/pushMessage")
	@Operation(summary = "푸시 알림 전송", description =
	"푸시 메시지를 전송합니다.")
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

	@PostMapping("/register")
	@Operation(summary = "디바이스 토큰 등록 API", description = "로그인한 사용자의 FCM 디바이스 토큰을 등록(업데이트)합니다.")
	public ResponseEntity<DataResponse<Void>> registerDeviceToken(
		@RequestParam String deviceToken
	) {
		fcmService.registerDeviceToken(deviceToken);
		return ResponseEntity.ok(DataResponse.ok());
	}
}
