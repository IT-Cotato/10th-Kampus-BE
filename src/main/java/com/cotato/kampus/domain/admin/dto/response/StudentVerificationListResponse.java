package com.cotato.kampus.domain.admin.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.admin.dto.StudentVerification;

public record StudentVerificationListResponse(
	List<StudentVerification> studentVerification,
	boolean hasNext
) {
	public static StudentVerificationListResponse from(Slice<StudentVerification> verifications){
		return new StudentVerificationListResponse(
			verifications.getContent(),
			verifications.hasNext()
		);
	}
}
