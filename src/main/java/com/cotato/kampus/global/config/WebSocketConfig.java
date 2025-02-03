package com.cotato.kampus.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.auth.nativeapp.AppUserDetails;
import com.cotato.kampus.global.auth.nativeapp.AppUserDetailsRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.JwtException;
import com.cotato.kampus.global.util.JwtUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtUtil jwtUtil;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/chatrooms");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket")
			.setAllowedOriginPatterns("*") // ✅ 모든 Origin 허용 (보안 강화 필요 시 특정 Origin만 추가)
			.withSockJS();
	}

	@Configuration
	static class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
		@Override
		protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
			messages
				.simpDestMatchers("/app/**").permitAll()  // 메시지 전송 권한
				.simpSubscribeDestMatchers("/chatrooms/**").permitAll() // 구독 권한
				.anyMessage().permitAll();
		}

		@Override
		protected boolean sameOriginDisabled() {
			return true; // CORS 설정을 위해 Same-Origin 정책 비활성화
		}
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					// JWT 인증 헤더 확인
					String authHeader = accessor.getFirstNativeHeader("Authorization");

					String token = extractToken(authHeader);
					validateToken(token);

					// 사용자 정보 추출 및 인증 객체 생성
					AppUserDetailsRequest detailsRequest = createPrincipalDetailsRequest(token);
					AppUserDetails appUserDetails = new AppUserDetails(detailsRequest);

					// Spring Security Authentication 생성 및 설정
					Authentication authentication = new UsernamePasswordAuthenticationToken(
						appUserDetails, null, appUserDetails.getAuthorities()
					);

					SecurityContextHolder.getContext().setAuthentication(authentication);

					log.info("WebSocket connection authenticated for user: {}", appUserDetails.getUniqueId());
					accessor.setUser(authentication);
				}
				return message;
			}
		});
	}

	private String extractToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new JwtException(ErrorCode.TOKEN_NOT_FOUND);
		}
		return authorizationHeader.substring(7).trim(); // Bearer 제거
	}

	private void validateToken(String token) {
		if (jwtUtil.isExpired(token)) {
			throw new JwtException(ErrorCode.TOKEN_EXPIRED);
		}
	}

	private AppUserDetailsRequest createPrincipalDetailsRequest(String token) {
		return AppUserDetailsRequest.of(
			jwtUtil.getUsername(token),
			jwtUtil.getUniqueId(token),
			UserRole.valueOf(jwtUtil.getRole(token))
		);
	}
}