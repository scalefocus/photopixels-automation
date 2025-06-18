package com.photopixels.constants;

public class BasePathsConstants {

    // Status
    public static String GET_STATUS = "/status";
    public static String GET_LOGS = "/logs";

    // Users
    public static String POST_REGISTER_USER = "/user/register";
    public static String POST_LOGIN = "/user/login";
    public static String DELETE_USER = "/user";
    public static String GET_USER_INFO = "/user/info";
    public static String POST_CHANGE_USER_PASSWORD = "/user/changepassword";
    public static String POST_RESET_USER_PASSWORD = "/user/resetpassword";
    public static String POST_FORGOT_USER_PASSWORD = "/user/forgotpassword";
    public static String POST_REFRESH_TOKEN = "/user/refresh";
    public static final String DELETE_EMPTY_TRASH = "/emptytrash";

    // Admin
    public static String POST_DISABLE_REGISTRATION = "/registration";
    public static String GET_USERS = "/users";
    public static String GET_USER = "/user/{Id}";
    public static String POST_RESET_PASSWORD_ADMIN = "/admin/resetpassword";
    public static String DELETE_USER_ADMIN = "/admin/user/{Id}";
    public static String POST_REGISTER_USER_ADMIN = "/admin/register";
    public static String POST_CHANGE_USER_ROLE = "/admin/role";
    public static String POST_ADJUST_STORAGE_QUOTA = "/admin/quota";

    // Sync
    public static String GET_CHANGES = "/revision/{RevisionId}";

    // Object operations
    public static String POST_UPLOAD_PHOTO = "/object";
    public static String DELETE_OBJECT = "/object/{Id}";
    public static String GET_OBJECT = "/object/{Id}";
    public static String PUT_UPDATE_OBJECT = "/object/{Id}";
    public static String GET_OBJECT_THUMBNAIL = "/object/{ObjectId}/thumbnail";
    public static String GET_OBJECT_DATA = "/object/{ObjectId}/data";
    public static String GET_OBJECTS_DATA = "/objects/data";
    public static String GET_OBJECTS = "/objects";
    public static String DELETE_TRASH_OBJECT = "/object/{objectid}/trash";
    public static String GET_TRASHED_OBJECTS  ="/objects/trashed";
    public static String POST_TRASH_OBJECT_REMOVE  ="/object/trash/remove";
    public static String POST_TRASH_DELETE_PERMANENT  ="/object/trash/deletePermanent";

    // TUS
    public static String POST_CREATE_UPLOAD = "/create_upload";
    public static String GET_RESUMABLE_UPLOADS = "/resumable_uploads";
    public static String PATCH_SEND_DATA = "send_data/{fileId}";
    public static String DELETE_SEND_DATA = "send_data/{fileId}";
}
