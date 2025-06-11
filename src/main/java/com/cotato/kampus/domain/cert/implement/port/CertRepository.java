package com.cotato.kampus.domain.cert.implement.port;

import java.util.Optional;

import com.cotato.kampus.domain.cert.domain.Cert;

public interface CertRepository {

	Optional<Cert> findByEmail(String email);

	Cert save(Cert cert);
}
