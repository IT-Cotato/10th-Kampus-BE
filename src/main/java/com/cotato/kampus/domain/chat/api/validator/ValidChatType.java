package com.cotato.kampus.domain.chat.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChatTypeValidator.class)
public @interface ValidChatType {
	String message() default "type은 POST 또는 PRODUCT만 가능합니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}