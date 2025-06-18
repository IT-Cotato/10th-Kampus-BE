package com.cotato.kampus.domain.cert.dao.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.cert.domain.Cert;
import com.cotato.kampus.domain.cert.dao.entity.CertEntity;
import com.cotato.kampus.domain.cert.implement.port.CertRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CertRepositoryImpl implements CertRepository {

	private final CertJpaRepository certJpaRepository;

	@Override
	public Optional<Cert> findByEmail(String email) {
		return certJpaRepository.findByEmail(email)
			.map(CertEntity::toDomain);
	}

	@Override
	public Cert save(Cert cert) {
		CertEntity entity = CertEntity.fromDomain(cert);
		return certJpaRepository.save(entity).toDomain();
	}

	@Override
	public void deleteAllByUserId(Long userId) {
		certJpaRepository.deleteAllByUserId(userId);
	}
}
