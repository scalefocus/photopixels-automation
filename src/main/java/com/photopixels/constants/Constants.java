package com.photopixels.constants;

import java.nio.file.Path;

public class Constants {

    public static final String PASSWORD = "Test12345!";

    // Base directory for uploads
    public static final Path UPLOAD_FILES_DIR = Path.of("upload_files");

    // Object Paths
    public static final String FILE_LOCATION = "upload_files/";
    public static final String FRENCH_FRIES_FILE = FILE_LOCATION + "french-fries.jpg";
    public static final String TRAINING_FILE = FILE_LOCATION + "training.jpg";
    public static final String UNNAMED_FILE = FILE_LOCATION + "unnamed.jpg";
    public static final String COCTAIL_FILE = FILE_LOCATION + "coctail.jpg";

    // TUS
    public static final String VALID_UPLOAD_METADATA = "fileExtension cG5n,fileName bmlrZTMucG5n,fileHash ZXhhbXBsZWhhc2g=,fileSize MzI0NDQ=,appleId ,androidId";
    public static final String VALID_UPLOAD_LENGTH = "46606";

    // Login Web
    public static final String OVERVIEW_HEADER = "Overview";
    public static final String WRONG_CREDENTIALS_MESSAGE = "Wrong email or password!";

    // Create User Web
    public static final String CREATE_NEW_USER = "Create A New User";

    //Pop-up Messages
    public static final String USER_CREATED = "User Created.";
    
    // Login Mobile
    public static final String LOGIN_ERROR_TITLE = "Error Login";
    public static final String LOGIN_ERROR_TEXT = "Error while Login. Please try again.";

}
