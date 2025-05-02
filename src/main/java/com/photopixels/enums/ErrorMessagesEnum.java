package com.photopixels.enums;

import lombok.Getter;

@Getter
public enum ErrorMessagesEnum {

    // Users
    PASSWORD_TOO_SHORT("PasswordTooShort", "Passwords must be at least 8 characters."),
    PASSWORD_REQUIRES_NON_ALPHANUMERIC("PasswordRequiresNonAlphanumeric", "Passwords must have at least one non alphanumeric character."),
    PASSWORD_REQUIRES_DIGIT("PasswordRequiresDigit", "Passwords must have at least one digit ('0'-'9')."),
    PASSWORD_REQUIRES_UPPER("PasswordRequiresUpper", "Passwords must have at least one uppercase ('A'-'Z')."),
    PASSWORD("Password", "The Password field is required."),
    NEW_PASSWORD("NewPassword", "The NewPassword field is required."),
    OLD_PASSWORD("OldPassword", "The OldPassword field is required."),
    PASSWORD_MISMATCH("PasswordMismatch", "Incorrect password."),
    EMAIL("Email", "The Email field is required."),
    USER_NOT_FOUND("UserNotFound", "User not found"),
    DUPLICATE_EMAIL("DuplicateEmail", "Email '%s' is already taken."),
    DUPLICATE_USER_NAME("DuplicateUserName", "Username '%s' is already taken."),
    NAME("Name", "The Name field is required."),
    CODE("Code", "The Code field is required."),
    INVALID_CODE("InvalidCode", "The provided code is invalid"),
    REFRESH_TOKEN("RefreshToken", "The RefreshToken field is required."),
    REGISTRATION_IS_DISABLED("RegistrationIsDisabled", "Cannot register new user until registration is enabled by admin!"),
    REQUEST("request", "The request field is required."),
    FILE("File", "The File field is required."),
    OBJECT_HASH("ObjectHash", "The ObjectHash field is required."),
    OBJECT_HASH_NOT_MATCH("ObjectHash", "Object hash does not match"),
    OBJECT_IDS("ObjectIds", "The ObjectIds field is required."),

    // Object operations
    PAGE_SIZE_INVALID("PageSize","The value '%s' is not valid for PageSize.");


    private final String key;

    private final String errorMessage;

    ErrorMessagesEnum(String k, String e) {
        key = k;
        errorMessage = e;
    }
}
