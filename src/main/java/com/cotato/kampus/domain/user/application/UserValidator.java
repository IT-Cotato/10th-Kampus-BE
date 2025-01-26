package com.cotato.kampus.domain.user.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.post.application.PostFinder;
import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserValidator {
	public final ApiUserResolver apiUserResolver;
	public final PostFinder postFinder;

	public void validateStudentVerification() {
		User user = apiUserResolver.getUser();

		if (user.getUserRole() == UserRole.UNVERIFIED)
			throw new AppException(ErrorCode.USER_UNVERIFIED);
	}

	public void validatePostAuthor(Long postId) {
		Long userId = apiUserResolver.getUserId();
		Long authorId = postFinder.getAuthorId(postId);

		// 작성자가 아닌 경우 예외 처리
		if (userId != authorId)
			throw new AppException(ErrorCode.POST_NOT_AUTHOR);
	}
}
