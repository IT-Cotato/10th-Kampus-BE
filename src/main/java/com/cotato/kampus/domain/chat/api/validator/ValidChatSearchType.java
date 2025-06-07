package com.cotato.kampus.domain.chat.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidChatSearchTypeValidator.class)
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface ValidChatSearchType {
	String message() default "유효하지 않은 채팅방 검색 타입입니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
} 