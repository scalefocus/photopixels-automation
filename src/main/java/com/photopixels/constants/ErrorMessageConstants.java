package com.photopixels.constants;

public class ErrorMessageConstants {

	// Users
	public static final String VALIDATION_ERRORS_TITLE = "One or more validation errors occurred.";
	public static final String NOT_FOUND_ERROR = "Not Found";
	public static final String CONFLICT_ERROR = "Conflict";
	public static String getDuplicateEmailError(String email) {
		return "Email '" + email + "' is already taken.";
	}

}
