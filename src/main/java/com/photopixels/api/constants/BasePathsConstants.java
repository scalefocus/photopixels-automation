package com.photopixels.api.constants;

public class BasePathsConstants {

    // Status
    public static String GET_STATUS = "/status";

    // Users
    public static String POST_REGISTER_USER = "/user/register";
    public static String POST_LOGIN = "/user/login";
    public static String DELETE_USER = "/user";
    public static String GET_USER_INFO = "/user/info";
    public static String POST_CHANGE_USER_PASSWORD = "/user/changepassword";
    public static String POST_RESET_USER_PASSWORD = "/user/resetpassword";
    public static String POST_FORGOT_USER_PASSWORD = "/user/forgotpassword";
    public static String POST_REFRESH_TOKEN = "/user/refresh";

    // Admin
    public static String POST_DISABLE_REGISTRATION = "/registration";
    public static String GET_USERS = "/users";
    public static String GET_USER = "/user/{Id}";
}
