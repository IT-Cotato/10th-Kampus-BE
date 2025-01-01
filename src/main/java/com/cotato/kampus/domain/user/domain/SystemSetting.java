package com.cotato.kampus.domain.user.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;
import com.cotato.kampus.domain.notification.enums.NotificationOption;

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
@Table(name = "system_setting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemSetting extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_setting_id")
	private Long id;

	@Column(name = "setting_id", nullable = false)
	private Long settingId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "chat_notification")
	private NotificationOption chatNotification;

	@Enumerated(EnumType.STRING)
	@Column(name = "comment_notification")
	private NotificationOption commentNotification;

	@Enumerated(EnumType.STRING)
	@Column(name = "trending_post_notification")
	private NotificationOption trendingPostNotification;

	@Enumerated(EnumType.STRING)
	@Column(name = "ad_agreement")
	private NotificationOption adAgreement;

	@Builder
	public SystemSetting(Long settingId, Long userId,
		NotificationOption chatNotification,
		NotificationOption commentNotification,
		NotificationOption trendingPostNotification,
		NotificationOption adAgreement) {
		this.settingId = settingId;
		this.userId = userId;
		this.chatNotification = chatNotification;
		this.commentNotification = commentNotification;
		this.trendingPostNotification = trendingPostNotification;
		this.adAgreement = adAgreement;
	}
}
