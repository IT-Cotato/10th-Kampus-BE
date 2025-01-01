package com.cotato.kampus.domain.notification.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;
import com.cotato.kampus.domain.notification.enums.NotificationStatus;
import com.cotato.kampus.domain.notification.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_pk_id")
	private Long id;

	@Column(name = "notification_id")
	private Long notificationId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "message", length = 255)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_status")
	private NotificationStatus notificationStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_type")
	private NotificationType notificationType;

	@Builder
	public Notification(Long notificationId, Long userId, String message,
		NotificationStatus notificationStatus,
		NotificationType notificationType) {
		this.notificationId = notificationId;
		this.userId = userId;
		this.message = message;
		this.notificationStatus = notificationStatus;
		this.notificationType = notificationType;
	}
}
