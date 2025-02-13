package com.cotato.kampus.domain.notification.application;

import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.notification.dto.FcmMessage;
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FcmService {

	private final ObjectMapper objectMapper;
	private final UserUpdater userUpdater;

	@Value("${fcm.file_path}")
	private String FIREBASE_CONFIG_PATH;

	@Value("${fcm.url}")
	private String FIREBASE_API_URI;

	@Value("${fcm.google_api}")
	private String GOOGLE_API_URI;

	// 메시지를 구성하고 토큰을 받아 FCM으로 메시지를 처리
	@Transactional
	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
		String message = makeMessage(targetToken, title, body);

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

		Request request = new Request.Builder()
			.url(FIREBASE_API_URI)
			.post(requestBody)
			.addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
			.addHeader(CONTENT_TYPE, "application/json; UTF-8")
			.build();

		Response response = client.newCall(request).execute();

		System.out.println(response.body().string());
	}

	// FCM 전송 정보를 기반으로 메시지 구성 (Object -> String)
	private String makeMessage(String targetToken, String title, String body)
		throws com.fasterxml.jackson.core.JsonProcessingException { // JsonParseException, JsonProcessingException

		FcmMessage.Notification notification = FcmMessage.Notification.of(title, body, null);
		FcmMessage.Message message = FcmMessage.Message.of(notification, targetToken);
		FcmMessage fcmMessage = FcmMessage.of(false, message);
		return objectMapper.writeValueAsString(fcmMessage);
	}

	// Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받음
	private String getAccessToken() throws IOException {
		try {
			final GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(
					FIREBASE_CONFIG_PATH).getInputStream())
				.createScoped(List.of(GOOGLE_API_URI));

			googleCredentials.refreshIfExpired();
			log.info("access token: {}", googleCredentials.getAccessToken());

			return googleCredentials.getAccessToken().getTokenValue();
		} catch (IOException exception) {
			// 로그 추가
			System.out.println("Error obtaining access token: " + exception.getMessage());

			throw new AppException(ErrorCode.GOOGLE_REQUEST_TOKEN_ERROR);
		}
	}

	@Transactional
	public void registerDeviceToken(String deviceToken){
		userUpdater.updateDeviceToken(deviceToken);
	}
}
