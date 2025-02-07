package com.cotato.kampus.domain.user.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agreement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agreement_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "personal_info_agreement", nullable = false)
	private boolean personalInfoAgreement;

	@Column(name = "privacy_policy_agreement", nullable = false)
	private boolean privacyPolicyAgreement;

	@Column(name = "terms_of_service_agreement", nullable = false)
	private boolean termsOfServiceAgreement;

	@Column(name = "marketing_agreement", nullable = false)
	private boolean marketingAgreement;

	@Builder
	public Agreement(Long userId, boolean personalInfoAgreement, boolean privacyPolicyAgreement,
		boolean termsOfServiceAgreement, boolean marketingAgreement) {
		this.userId = userId;
		this.personalInfoAgreement = personalInfoAgreement;
		this.privacyPolicyAgreement = privacyPolicyAgreement;
		this.termsOfServiceAgreement = termsOfServiceAgreement;
		this.marketingAgreement = marketingAgreement;
	}
}