package com.photopixels.helpers;

import java.util.Properties;

import static org.testng.Assert.fail;

public class InputDataHelper {

    private static final String DATA_PROPS = "data.properties";

    private Properties props;

    public InputDataHelper() {
        props = new PropertiesUtils().loadProps(DATA_PROPS);
    }

    private String getProperty(String key) {
        String value = props.getProperty(key);

        if (value != null) {
            return value;
        } else {
            fail(String.format("Could not find property '%s'", key));
            return null;
        }
    }

    public String getUserFullName() {
        return getProperty("userFullName");
    }

    public String getUsername() {
        return getProperty("username");
    }

    public String getPassword() {
        return getProperty("password");
    }

    public String getNewPassword() {
        return getProperty("newPassword");
    }

    public String getUsernameAdmin() {
        return getProperty("usernameAdmin");
    }

    public String getPasswordAdmin() {
        return getProperty("passwordAdmin");
    }

    public String getInvalidEmail() {
        return getProperty("invalidEmail");
    }

    public String getInvalidPassword() {
        return getProperty("invalidPassword");
    }
}
