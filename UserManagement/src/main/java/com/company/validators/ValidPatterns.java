package com.company.validators;

public abstract class ValidPatterns {

	public static final String PATTERN_WITHOUT_SPECIAL_CHARACTERS_AND_DIGITS = "^[A-Za-z+]*$";
	public static final String PATTERN_WITHOUT_SPECIAL_CHARACTERS = "^[A-Za-z0-9]*$";
	public static final String PATTERN_WITH_DIGITS_AND_ALLOWED_SPECIAL_CHARACTERS = "^[_A-Za-z0-9]*$";
	public static final String PATTERN_FOR_VALID_INDIAN_MOBILE_NUMBER = "\\d{10}";

}
