package com.photopixels.api.enums;

import lombok.Getter;

public enum ErrorMessagesEnum {
    // Users
    PASSWORD_TOO_SHORT("PasswordTooShort", "Passwords must be at least 8 characters."),
    PASSWORD_REQUIRES_NON_ALPHANUMERIC("PasswordRequiresNonAlphanumeric", "Passwords must have at least one non alphanumeric character."),
    PASSWORD_REQUIRES_DIGIT("PasswordRequiresDigit", "Passwords must have at least one digit ('0'-'9')."),
    PASSWORD_REQUIRES_UPPER("PasswordRequiresUpper", "Passwords must have at least one uppercase ('A'-'Z')."),
    PASSWORD("Password", "The Password field is required."),
    EMAIL("Email", "The Email field is required."),
    DUPLICATE_EMAIL("DuplicateEmail", "Email '%s' is already taken."),
    DUPLICATE_USER_NAME("DuplicateUserName", "Username '%s' is already taken."),
    NAME("Name", "The Name field is required.");

    @Getter
    private String key;

    @Getter
    private String errorMessage;

    ErrorMessagesEnum(String k, String e) {
        key = k;
        errorMessage = e;
    }
}
