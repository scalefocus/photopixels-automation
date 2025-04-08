package com.photopixels.constants;

public class Constants {

    public static final String PASSWORD = "Test12345!";

    // Object Paths
    public static final String FILE_LOCATION = "upload_files/";
    public static final String FRENCH_FRIES_FILE = FILE_LOCATION + "french-fries.jpg";
    public static final String TRAINING_FILE = FILE_LOCATION + "training.jpg";
    public static final String UNNAMED_FILE = FILE_LOCATION + "unnamed.jpg";

    // Login Web
    public static final String OVERVIEW_HEADER = "Overview";
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong email or password!";
    public static final String CREATE_NEW_USER = "Create A New User";

    //Pop-up Messages
    public static final String USER_CREATED = "User Created.";

    //Error Messages
    public static final String EIGHT_CHARACTERS_REQUIREMENT = "Passwords must be at least 8 characters.";
    public static final String ALPHANUMERIC_RREQUIREMENT = "Passwords must have at least one non alphanumeric character.";
    public static final String ONE_DIGIT_REQUIREMENT = "Passwords must have at least one digit ('0'-'9').";
    public static final String ONE_UPPERCASE_REQUIREMENT = "Passwords must have at least one uppercase ('A'-'Z').";
    public static String getDuplicateEmailError(String email) {
        return "Email '" + email + "' is already taken.";
    }
}
