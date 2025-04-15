package com.photopixels.constants;

public class ErrorMessageConstants {

	// Users
	public static final String VALIDATION_ERRORS_TITLE = "One or more validation errors occurred.";
	public static final String NOT_FOUND_ERROR = "Not Found";
	public static final String CONFLICT_ERROR = "Conflict";
	public static final String EIGHT_CHARACTERS_REQUIREMENT = "Passwords must be at least 8 characters.";
	public static final String ALPHANUMERIC_RREQUIREMENT = "Passwords must have at least one non alphanumeric character.";
	public static final String ONE_DIGIT_REQUIREMENT = "Passwords must have at least one digit ('0'-'9').";
	public static final String ONE_UPPERCASE_REQUIREMENT = "Passwords must have at least one uppercase ('A'-'Z').";
	public static String getDuplicateEmailError(String email) {
		return "Email '" + email + "' is already taken.";
	}

}
