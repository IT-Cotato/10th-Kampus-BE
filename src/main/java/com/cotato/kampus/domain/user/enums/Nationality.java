package com.cotato.kampus.domain.user.enums;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

public enum Nationality {
	SAUDI_ARABIA("Saudi Arabia", "SA"),    // Arabic
	BULGARIA("Bulgaria", "BG"),            // Bulgarian
	CZECH_REPUBLIC("Czech Republic", "CZ"), // Czech
	DENMARK("Denmark", "DK"),              // Danish
	GERMANY("Germany", "DE"),              // German
	GREECE("Greece", "GR"),                // Greek
	UNITED_KINGDOM("United Kingdom", "GB"), // English (British)
	UNITED_STATES("United States", "US"),   // English (American)
	SPAIN("Spain", "ES"),                  // Spanish
	ESTONIA("Estonia", "EE"),              // Estonian
	FINLAND("Finland", "FI"),              // Finnish
	FRANCE("France", "FR"),                // French
	HUNGARY("Hungary", "HU"),              // Hungarian
	INDONESIA("Indonesia", "ID"),          // Indonesian
	ITALY("Italy", "IT"),                  // Italian
	JAPAN("Japan", "JP"),                  // Japanese
	KOREA("South Korea", "KR"),            // Korean
	LITHUANIA("Lithuania", "LT"),          // Lithuanian
	LATVIA("Latvia", "LV"),                // Latvian
	NORWAY("Norway", "NO"),                // Norwegian
	NETHERLANDS("Netherlands", "NL"),       // Dutch
	POLAND("Poland", "PL"),                // Polish
	BRAZIL("Brazil", "BR"),                // Portuguese (Brazilian)
	PORTUGAL("Portugal", "PT"),            // Portuguese
	ROMANIA("Romania", "RO"),              // Romanian
	RUSSIA("Russia", "RU"),                // Russian
	SLOVAKIA("Slovakia", "SK"),            // Slovak
	SLOVENIA("Slovenia", "SI"),            // Slovenian
	SWEDEN("Sweden", "SE"),                // Swedish
	TURKEY("Turkey", "TR"),                // Turkish
	UKRAINE("Ukraine", "UA"),              // Ukrainian
	CHINA("China", "CN");                  // Chinese (both simplified and traditional)

	private final String name;
	private final String code;

	Nationality(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public static Nationality fromCode(String code) {
		for (Nationality nationality : values()) {
			if (nationality.code.equalsIgnoreCase(code)) {
				return nationality;
			}
		}
		throw new AppException(ErrorCode.INVALID_NATIONALITY_CODE);
	}

	public static Nationality fromName(String name) {
		for (Nationality nationality : values()) {
			if (nationality.name.equalsIgnoreCase(name)) {
				return nationality;
			}
		}
		throw new AppException(ErrorCode.INVALID_NATIONALITY_NAME);
	}
}