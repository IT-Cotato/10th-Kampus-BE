package com.cotato.kampus.domain.user.enums;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

public enum PreferredLanguage {
	ARABIC("Arabic", "AR"),
	BULGARIAN("Bulgarian", "BG"),
	CZECH("Czech", "CS"),
	DANISH("Danish", "DA"),
	GERMAN("German", "DE"),
	GREEK("Greek", "EL"),
	ENGLISH_BRITISH("English (British)", "EN-GB"),
	ENGLISH_AMERICAN("English (American)", "EN-US"),
	SPANISH("Spanish", "ES"),
	ESTONIAN("Estonian", "ET"),
	FINNISH("Finnish", "FI"),
	FRENCH("French", "FR"),
	HUNGARIAN("Hungarian", "HU"),
	INDONESIAN("Indonesian", "ID"),
	ITALIAN("Italian", "IT"),
	JAPANESE("Japanese", "JA"),
	KOREAN("Korean", "KO"),
	LITHUANIAN("Lithuanian", "LT"),
	LATVIAN("Latvian", "LV"),
	NORWEGIAN_BOKMAL("Norwegian Bokmål", "NB"),
	DUTCH("Dutch", "NL"),
	POLISH("Polish", "PL"),
	PORTUGUESE_BRAZILIAN("Portuguese (Brazilian)", "PT-BR"),
	PORTUGUESE("Portuguese", "PT-PT"),
	ROMANIAN("Romanian", "RO"),
	RUSSIAN("Russian", "RU"),
	SLOVAK("Slovak", "SK"),
	SLOVENIAN("Slovenian", "SL"),
	SWEDISH("Swedish", "SV"),
	TURKISH("Turkish", "TR"),
	UKRAINIAN("Ukrainian", "UK"),
	CHINESE_SIMPLIFIED("Chinese (simplified)", "ZH-HANS"),
	CHINESE_TRADITIONAL("Chinese (traditional)", "ZH-HANT");

	private final String name;
	private final String code;

	PreferredLanguage(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public static PreferredLanguage fromCode(String code) {
		for (PreferredLanguage language : values()) {
			if (language.code.equalsIgnoreCase(code)) {
				return language;
			}
		}
		throw new AppException(ErrorCode.INVALID_LANGUAGE_CODE);
	}

	public static PreferredLanguage fromName(String name) {
		for (PreferredLanguage language : values()) {
			if (language.name.equalsIgnoreCase(name)) {
				return language;
			}
		}
		throw new AppException(ErrorCode.INVALID_LANGUAGE_NAME);
	}
}