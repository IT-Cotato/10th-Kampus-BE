package com.cotato.kampus.domain.notification.dto;

public record FcmMessage(
	boolean validateOnly,
	Message message
) {
	public static FcmMessage of(boolean validateOnly, Message message) {
		return new FcmMessage(validateOnly, message);
	}

	public record Message(
		Notification notification,
		String token
	) {
		public static Message of(Notification notification, String token) {
			return new Message(notification, token);
		}
	}

	public record Notification(
		String title,
		String body,
		String image
	) {
		public static Notification of(String title, String body, String image) {
			return new Notification(
				title, body, image
			);
		}
	}
}
