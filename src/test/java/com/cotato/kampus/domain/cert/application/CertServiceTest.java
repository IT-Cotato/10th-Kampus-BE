package com.cotato.kampus.domain.cert.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.domain.TestCertHelper;
import com.cotato.kampus.domain.cert.enums.UnivMail;
import com.cotato.kampus.domain.cert.implement.CertFinder;
import com.cotato.kampus.domain.cert.implement.CertMailSender;
import com.cotato.kampus.domain.cert.implement.CertManager;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
class CertServiceTest {

	@InjectMocks
	private CertService certService;

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private UserValidator userValidator;

	@Mock
	private CertFinder certFinder;

	@Mock
	private CertManager certManager;

	@Mock
	private CertMailSender certMailSender;

	private MockedStatic<UnivMail> univMailMock;

	private UserDto unverifiedUser;

	private UserDto verifiedUser;

	@BeforeEach
	void setUp() {
		unverifiedUser = TestUserHelper.createUserDto(1L, null, UserRole.UNVERIFIED);
		verifiedUser = TestUserHelper.createUserDto(1L, null, UserRole.VERIFIED);
		univMailMock = mockStatic(UnivMail.class);
	}

	@AfterEach
	void tearDown() {
		univMailMock.close();
	}

	@Test
	@DisplayName("이메일 발송 성공 - 새로운 인증 요청")
	void sendMain_Success_NewRequest() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findOptionalByEmail(email)).thenReturn(null);

		univMailMock.when(() -> UnivMail.exists(univCode)).thenReturn(true);
		univMailMock.when(() -> UnivMail.validateUnivCode(univCode)).thenCallRealMethod();
		univMailMock.when(() -> UnivMail.getDomains(univCode)).thenReturn(List.of("test"));

		// when
		certService.sendMail(univCode, email);

		// then
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);

		// 새로운 인증 정보 생성 검증
		verify(certManager).append(eq(email), eq(univCode), anyString(), eq(unverifiedUser.id()));
		// 인증 메일 발송 검증
		verify(certMailSender).sendVerificationMail(eq(email), anyString());
	}

	@Test
	@DisplayName("이메일 발송 성공 - 기존 인증 요청 갱신")
	void sendMain_Success_UpdatedExisting() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";

		// 기존 인증 요청 정보 (미인증 상태)
		Cert cert = TestCertHelper.createCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id());

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findOptionalByEmail(email)).thenReturn(cert);

		univMailMock.when(() -> UnivMail.exists(univCode)).thenReturn(true);
		univMailMock.when(() -> UnivMail.validateUnivCode(univCode)).thenCallRealMethod();
		univMailMock.when(() -> UnivMail.getDomains(univCode)).thenReturn(List.of("test"));

		// when
		certService.sendMail(univCode, email);

		// then
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);

		// 기존 인증 정보 갱신 검증
		ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
		verify(certManager).updateCodeAndExpiration(eq(cert), codeCaptor.capture());

		verify(certMailSender).sendVerificationMail(eq(email), codeCaptor.capture());

		String newCode = codeCaptor.getValue();
		assertThat(newCode).matches("\\d{4}");
		assertThat(newCode).isNotEqualTo(code);

	}

	@Test
	@DisplayName("이메일 발송 실패 - 이미 인증된 유저")
	void sendMain_Failure_AlreadyVerified() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";

		when(apiUserResolver.getCurrentUserDto()).thenReturn(verifiedUser);
		doThrow(new AppException(ErrorCode.USER_ALREADY_VERIFIED))
			.when(userValidator).validateDuplicateStudentVerification(verifiedUser);

		univMailMock.when(() -> UnivMail.exists(univCode)).thenReturn(true);
		univMailMock.when(() -> UnivMail.validateUnivCode(univCode)).thenCallRealMethod();
		univMailMock.when(() -> UnivMail.getDomains(univCode)).thenReturn(List.of("test"));

		// when & Then
		AppException ex = assertThrows(AppException.class, () -> certService.sendMail(univCode, email));
		assertEquals(ErrorCode.USER_ALREADY_VERIFIED, ex.getErrorCode());

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(verifiedUser);

		univMailMock.verify(() -> UnivMail.validateUnivCode(univCode), never());
		univMailMock.verify(() -> UnivMail.getDomains(univCode), never());

		verify(certFinder, never()).findOptionalByEmail(anyString());
		verify(certMailSender, never()).sendVerificationMail(anyString(), anyString());
	}

	@Test
	@DisplayName("이메일 발송 실패 - 도메인 불일치")
	void sendMain_Failure_InvalidDomain() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);

		univMailMock.when(() -> UnivMail.exists(univCode)).thenReturn(true);
		univMailMock.when(() -> UnivMail.validateUnivCode(univCode)).thenCallRealMethod();
		univMailMock.when(() -> UnivMail.getDomains(univCode)).thenReturn(List.of("real"));

		// when & Then
		AppException ex = assertThrows(AppException.class, () -> certService.sendMail(univCode, email));
		assertEquals(ErrorCode.INVALID_UNIVERSITY_EMAIL_DOMAIN, ex.getErrorCode());

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);

		verify(certFinder, never()).findOptionalByEmail(anyString());
		verify(certMailSender, never()).sendVerificationMail(anyString(), anyString());
	}
}