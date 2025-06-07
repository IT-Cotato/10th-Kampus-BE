package com.cotato.kampus.domain.chat.api.validator;

import com.cotato.kampus.domain.chat.enums.ChatSearchType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidChatSearchTypeValidator implements ConstraintValidator<ValidChatSearchType, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return false;
		try {
			ChatSearchType.valueOf(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
} 