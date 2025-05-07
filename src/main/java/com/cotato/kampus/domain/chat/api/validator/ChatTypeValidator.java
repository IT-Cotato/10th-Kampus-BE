package com.cotato.kampus.domain.chat.api.validator;

import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChatTypeValidator implements ConstraintValidator<ValidChatType, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// null 체크
		if (value == null || value.trim().isEmpty()) {
			return false;
		}

		try {
			ChatType.valueOf(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}