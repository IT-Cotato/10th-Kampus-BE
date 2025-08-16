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

import com.cotato.kampus.domain.admin.application.VerificationPhotoFinder;
import com.cotato.kampus.domain.admin.dto.VerificationPhotoDto;
import com.cotato.kampus.domain.admin.dto.VerificationWithPhoto;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteManager;
import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.domain.TestCertHelper;
import com.cotato.kampus.domain.cert.enums.UnivMail;
import com.cotato.kampus.domain.cert.implement.CertFinder;
import com.cotato.kampus.domain.cert.implement.CertMailSender;
import com.cotato.kampus.domain.cert.implement.CertManager;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.university.application.UnivFinder;
import com.cotato.kampus.domain.user.application.UserUpdater;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.VerificationStatus;
import com.cotato.kampus.domain.user.enums.VerificationType;
import com.cotato.kampus.domain.verification.application.VerificationRecordFinder;
import com.cotato.kampus.domain.verification.application.VerificationRecordManager;
import com.cotato.kampus.domain.verification.dto.VerificationRecordDto;
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

	@Mock
	private UnivFinder univFinder;

	@Mock
	private UserUpdater userUpdater;

	@Mock
	private VerificationRecordManager verificationRecordManager;

	@Mock
	private VerificationRecordFinder verificationRecordFinder;

	@Mock
	private VerificationPhotoFinder verificationPhotoFinder;

	@Mock
	private BoardFinder boardFinder;

	@Mock
	private BoardFavoriteManager boardFavoriteManager;

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

	@Test
	@DisplayName("이메일 인증 성공")
	void verifyEmailCode_Success() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";
		Long universityId = 401L;
		Long universityBoardId = 1L;
		Cert cert = spy(
			TestCertHelper.createCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id()));

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findByEmail(email)).thenReturn(cert);
		when(univFinder.findUniversityId(cert.getUnivCode())).thenReturn(universityId);
		when(boardFinder.findUniversityBoardId(universityId)).thenReturn(universityBoardId);

		// when
		certService.verifyEmailCode(email, code);

		// then
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(cert).validateNotCertified();
		verify(cert).validateExpired();
		verify(cert).validateCode(code);
		verify(userUpdater).updateVerificationStatus(unverifiedUser.id(), universityId);
		verify(verificationRecordManager).appendEmailType(unverifiedUser.id(), universityId);
		verify(boardFinder).findUniversityBoardId(universityId);
		verify(boardFavoriteManager).appendFavoriteBoard(unverifiedUser.id(), universityBoardId);
	}

	@Test
	@DisplayName("이메일 인증 성공 - 대학 게시판이 없는 경우")
	void verifyEmailCode_Success_UnivBoardNonExist() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";
		Long universityId = 401L;
		Cert cert = spy(
			TestCertHelper.createCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id()));

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findByEmail(email)).thenReturn(cert);
		when(univFinder.findUniversityId(cert.getUnivCode())).thenReturn(universityId);
		when(boardFinder.findUniversityBoardId(universityId)).thenReturn(null);

		// when
		certService.verifyEmailCode(email, code);

		// then
		// 호출되어야 함
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(cert).validateNotCertified();
		verify(cert).validateExpired();
		verify(cert).validateCode(code);
		verify(userUpdater).updateVerificationStatus(unverifiedUser.id(), universityId);
		verify(verificationRecordManager).appendEmailType(unverifiedUser.id(), universityId);
		verify(boardFinder).findUniversityBoardId(universityId);

		// 호출되지 않아야 함
		verify(boardFavoriteManager, never()).appendFavoriteBoard(anyLong(), anyLong());

	}

	@Test
	@DisplayName("이메일 인증 실패 - 이미 인증된 유저")
	void verifyEmailCode_Failure_AlreadyVerified() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";
		Long universityId = 401L;
		Long universityBoardId = 1L;
		Cert cert = spy(
			TestCertHelper.createCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id()));

		when(apiUserResolver.getCurrentUserDto()).thenReturn(verifiedUser);
		doThrow(new AppException(ErrorCode.USER_ALREADY_VERIFIED))
			.when(userValidator).validateDuplicateStudentVerification(verifiedUser);

		// when & Then
		AppException ex = assertThrows(AppException.class, () -> certService.verifyEmailCode(email, code));
		assertEquals(ErrorCode.USER_ALREADY_VERIFIED, ex.getErrorCode());

		// 호출되어야 함
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(verifiedUser);

		// 호출되지 않아야 함
		verify(certFinder, never()).findByEmail(email);
		verify(cert, never()).validateNotCertified();
		verify(cert, never()).validateExpired();
		verify(cert, never()).validateCode(code);
		verify(userUpdater, never()).updateVerificationStatus(unverifiedUser.id(), universityId);
		verify(verificationRecordManager, never()).appendEmailType(unverifiedUser.id(), universityId);
		verify(boardFinder, never()).findUniversityBoardId(universityId);
		verify(boardFavoriteManager, never()).appendFavoriteBoard(unverifiedUser.id(), universityBoardId);
	}

	@Test
	@DisplayName("이메일 인증 실패 - 유효시간 만료")
	void verifyEmailCode_Failure_Expired() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";
		Long universityId = 401L;
		Long universityBoardId = 1L;
		Cert expiredCert = spy(
			TestCertHelper.createExpiredCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id()));

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findByEmail(email)).thenReturn(expiredCert);

		// when & Then
		AppException ex = assertThrows(AppException.class, () -> certService.verifyEmailCode(email, code));
		assertEquals(ErrorCode.VERIFICATION_CODE_EXPIRED, ex.getErrorCode());

		// 호출되어야 함
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(certFinder).findByEmail(email);
		verify(expiredCert).validateNotCertified();
		verify(expiredCert).validateExpired();

		// 호출되지 않아야 함
		verify(expiredCert, never()).validateCode(code);
		verify(certManager, never()).certify(expiredCert);
		verify(userUpdater, never()).updateVerificationStatus(unverifiedUser.id(), universityId);
		verify(verificationRecordManager, never()).appendEmailType(unverifiedUser.id(), universityId);
		verify(boardFinder, never()).findUniversityBoardId(universityId);
		verify(boardFavoriteManager, never()).appendFavoriteBoard(unverifiedUser.id(), universityBoardId);
	}

	@Test
	@DisplayName("이메일 인증 실패 - 코드 불일치")
	void verifyEmailCode_Failure_InvalidCode() {
		// given
		String univCode = "TEST";
		String email = "test@test.com";
		String code = "1234";
		String invalidCode = "4321";
		Long universityId = 401L;
		Long universityBoardId = 1L;
		Cert cert = spy(
			TestCertHelper.createCert(unverifiedUser.id(), email, univCode, code, false, unverifiedUser.id()));

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(certFinder.findByEmail(email)).thenReturn(cert);

		// when & Then
		AppException ex = assertThrows(AppException.class, () -> certService.verifyEmailCode(email, invalidCode));
		assertEquals(ErrorCode.INVALID_VERIFICATION_CODE, ex.getErrorCode());

		// 호출되어야 함
		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(certFinder).findByEmail(email);
		verify(cert).validateNotCertified();
		verify(cert).validateExpired();
		verify(cert).validateCode(invalidCode);

		// 호출되지 않아야 함
		verify(certManager, never()).certify(cert);
		verify(userUpdater, never()).updateVerificationStatus(unverifiedUser.id(), universityId);
		verify(verificationRecordManager, never()).appendEmailType(unverifiedUser.id(), universityId);
		verify(boardFinder, never()).findUniversityBoardId(universityId);
		verify(boardFavoriteManager, never()).appendFavoriteBoard(unverifiedUser.id(), universityBoardId);
	}

	@Test
	@DisplayName("서류 반려 사유 조회 - 성공")
	void getRejectReason_Success() {
		// given
		Long verificationRecordId = 1L;
		Long universityId = 401L;
		String univCode = "TEST";
		String rejectReason = "학생증 사진이 흐리게 찍혀서 확인이 불가능합니다.";

		VerificationRecordDto verificationRecordDto = new VerificationRecordDto(
			verificationRecordId,
			unverifiedUser.id(),
			universityId,
			VerificationType.PHOTO,
			VerificationStatus.REJECTED,
			rejectReason
		);

		VerificationPhotoDto verificationPhotoDto = new VerificationPhotoDto(
			verificationRecordId,
			"photoUrl"
		);


		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		when(verificationRecordFinder.findRecentPhotoRecord(unverifiedUser.id())).thenReturn(verificationRecordDto);
		when(univFinder.findUniversityCode(verificationRecordDto.universityId())).thenReturn(univCode);
		when(verificationPhotoFinder.findByRecordId(verificationRecordId)).thenReturn(verificationPhotoDto);

		// when
		VerificationWithPhoto result = certService.getRejectReason();

		// then
		assertThat(result).isNotNull();
		assertThat(result.verificationRecordId()).isEqualTo(verificationRecordId);
		assertThat(result.universityId()).isEqualTo(universityId);
		assertThat(result.universityCode()).isEqualTo(univCode);
		assertThat(result.rejectReason()).isEqualTo(rejectReason);
		assertThat(result.verificationType()).isEqualTo(VerificationType.PHOTO);
		assertThat(result.verificationStatus()).isEqualTo(VerificationStatus.REJECTED);
		assertThat(result.rejectReason()).isEqualTo(rejectReason);

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(verificationRecordFinder).findRecentPhotoRecord(unverifiedUser.id());
		verify(univFinder).findUniversityCode(universityId);
		verify(verificationPhotoFinder).findByRecordId(verificationRecordId);
	}

	@Test
	@DisplayName("서류 반려 사유 조회 - 이미 인증된 유저 실패")
	void getRejectReason_Failure_AlreadyVerified() {
		// given
		when(apiUserResolver.getCurrentUserDto()).thenReturn(verifiedUser);
		doThrow(new AppException(ErrorCode.USER_ALREADY_VERIFIED))
			.when(userValidator).validateDuplicateStudentVerification(verifiedUser);

		// when & then
		AppException ex = assertThrows(AppException.class, () -> certService.getRejectReason());
		assertEquals(ErrorCode.USER_ALREADY_VERIFIED, ex.getErrorCode());

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator, never()).validateDuplicateStudentVerification(unverifiedUser);
		verify(verificationRecordFinder, never()).findRecentPhotoRecord(anyLong());
		verify(univFinder, never()).findUniversityCode(anyLong());
		verify(verificationPhotoFinder, never()).findByRecordId(anyLong());
	}

	@Test
	@DisplayName("서류 반려 사유 조회 - 반려된 서류 인증 기록이 없는 경우")
	void getRejectReason_Failure_() {
		// given
		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUser);
		doThrow(new AppException(ErrorCode.REJECTED_RECORD_NOT_FOUND))
			.when(verificationRecordFinder).findRecentPhotoRecord(unverifiedUser.id());

		// when & then
		AppException ex = assertThrows(AppException.class, () -> certService.getRejectReason());
		assertEquals(ErrorCode.REJECTED_RECORD_NOT_FOUND, ex.getErrorCode());

		verify(apiUserResolver).getCurrentUserDto();
		verify(userValidator).validateDuplicateStudentVerification(unverifiedUser);
		verify(verificationRecordFinder).findRecentPhotoRecord(unverifiedUser.id());
		verify(univFinder, never()).findUniversityCode(anyLong());
		verify(verificationPhotoFinder, never()).findByRecordId(anyLong());
	}

}