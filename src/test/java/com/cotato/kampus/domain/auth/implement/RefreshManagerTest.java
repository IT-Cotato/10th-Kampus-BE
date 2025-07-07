package com.cotato.kampus.domain.auth.implement;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.auth.dao.RefreshRepository;
import com.cotato.kampus.domain.auth.domain.RefreshEntity;
import com.cotato.kampus.domain.auth.factory.TokenTestDataFactory;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshManager 테스트")
class RefreshManagerTest {

	@InjectMocks
	private RefreshManager refreshManager;

	@Mock
	private RefreshRepository refreshRepository;

	@Test
	@DisplayName("리프레시 엔티티 추가 - 기존 토큰이 없는 경우에는 새로 저장")
	void addRefreshEntity_NewUser_Success() {
		// given
		String uniqueId = TokenTestDataFactory.TEST_UNIQUE_ID;
		String username = TokenTestDataFactory.TEST_USERNAME;
		String refreshToken = TokenTestDataFactory.createRefreshToken();
		Long expiration = TokenTestDataFactory.REFRESH_TOKEN_EXP;

		when(refreshRepository.existsByUniqueId(uniqueId)).thenReturn(false);

		// when
		refreshManager.addRefreshEntity(uniqueId, username, refreshToken, expiration);

		// then
		ArgumentCaptor<RefreshEntity> entityCaptor = ArgumentCaptor.forClass(RefreshEntity.class);
		verify(refreshRepository).save(entityCaptor.capture());
		verify(refreshRepository, never()).deleteByUniqueId(uniqueId);

		RefreshEntity savedEntity = entityCaptor.getValue();
		assertThat(savedEntity.getUniqueId()).isEqualTo(uniqueId);
		assertThat(savedEntity.getUsername()).isEqualTo(username);
		assertThat(savedEntity.getRefresh()).isEqualTo(refreshToken);
		assertThat(savedEntity.getExpiration()).isNotNull();
	}

	@Test
	@DisplayName("리프레시 엔티티 추가 - 기존 토큰이 있는 경우에는 삭제 후 저장")
	void addRefreshEntity_ExistingUser_DeleteAndSave() {
		// given
		String uniqueId = TokenTestDataFactory.TEST_UNIQUE_ID;
		String username = TokenTestDataFactory.TEST_USERNAME;
		String refreshToken = TokenTestDataFactory.createRefreshToken();
		Long expiration = TokenTestDataFactory.REFRESH_TOKEN_EXP;

		when(refreshRepository.existsByUniqueId(uniqueId)).thenReturn(true);

		// when
		refreshManager.addRefreshEntity(uniqueId, username, refreshToken, expiration);

		// then
		verify(refreshRepository).deleteByUniqueId(uniqueId);

		ArgumentCaptor<RefreshEntity> entityCaptor = ArgumentCaptor.forClass(RefreshEntity.class);
		verify(refreshRepository).save(entityCaptor.capture());

		RefreshEntity savedEntity = entityCaptor.getValue();
		assertThat(savedEntity.getUniqueId()).isEqualTo(uniqueId);
		assertThat(savedEntity.getUsername()).isEqualTo(username);
		assertThat(savedEntity.getRefresh()).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("존재하는 토큰 삭제 성공")
	void deleteRefreshToken_Success() {
		// given
		String uniqueId = "testUniqueId";
		when(refreshRepository.existsByUniqueId(uniqueId)).thenReturn(true);

		// when & then
		assertDoesNotThrow(() -> refreshManager.deleteRefreshToken(uniqueId));

		// verify
		verify(refreshRepository).existsByUniqueId(uniqueId);
		verify(refreshRepository).deleteByUniqueId(uniqueId);
	}

	@Test
	@DisplayName("존재하지 않는 토큰 삭제 시 예외 발생")
	void deleteRefreshToken_ThrowsException_WhenTokenNotExists() {
		// given
		String uniqueId = "nonExistentUniqueId";
		when(refreshRepository.existsByUniqueId(uniqueId)).thenReturn(false);

		// when & then
		AppException exception = assertThrows(AppException.class,
			() -> refreshManager.deleteRefreshToken(uniqueId));

		// verify
		verify(refreshRepository).existsByUniqueId(uniqueId);
		// deleteByUniqueId는 호출되지 않아야 함
		verify(refreshRepository, org.mockito.Mockito.never()).deleteByUniqueId(uniqueId);
	}
}