package com.photopixels.base;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.admin.DeleteUserAdminSteps;
import com.photopixels.api.steps.admin.GetUsersSteps;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static io.restassured.RestAssured.baseURI;

public class ApiBaseTest extends BaseTest{

    @BeforeSuite(alwaysRun = true)
    public void initSuiteApi() {
        baseURI = baseUri;

        String logEnabled = System.getProperty("isLogEnabled");

        if (logEnabled == null) {
            logEnabled = configProperties.getProperty("isLogEnabled");
        }

        if (Boolean.parseBoolean(logEnabled)) {
            RestAssured.replaceFiltersWith(new RequestLoggingFilter(LogDetail.ALL),
                    new ResponseLoggingFilter(LogDetail.ALL));
        } else {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }

        prepareUsers();
    }

    // TODO: Move that to be available for web and mobile suites
    protected void prepareUsers() {
        try {
            PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
            postRegisterUserSteps.registerUser(inputData.getUserFullName(),
                    inputData.getUsername(), inputData.getPassword());
        } catch (AssertionError e) {
            // User already registered
            // TODO: Add proper check for duplicate user error
        }
    }

    protected String getUserToken() {
        return getToken(inputData.getUsername(), inputData.getPassword());
    }

    protected String getAdminToken() {
        return getToken(inputData.getUsernameAdmin(), inputData.getPasswordAdmin());
    }

    protected String getToken(String username, String password) {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

        return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
    }

    protected String getUserId(String username) {
        GetUsersSteps getUsersSteps = new GetUsersSteps(getAdminToken());
        GetUserResponseDto[] getUserResponseDtos = getUsersSteps.getUsers();

        return Arrays.stream(getUserResponseDtos)
                .filter(user -> user.getEmail().equals(username)).findFirst().get().getId();
    }

    protected void deleteRegisteredUsers(Map<String, String> registeredUsers) {
        if (!registeredUsers.isEmpty()) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                String token = getToken(entry.getKey(), entry.getValue());

                DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
                deleteUserSteps.deleteUser(entry.getValue());
            }
        }
    }

    protected void deleteRegisteredUsersAdmin(List<String> registeredUsers) {
        if (!registeredUsers.isEmpty()) {
            String token = getAdminToken();

            for (String username : registeredUsers) {
                String userId = getUserId(username);

                DeleteUserAdminSteps deleteUserAdminSteps = new DeleteUserAdminSteps(token);
                deleteUserAdminSteps.deleteUserAdmin(userId);
            }
        }
    }

    protected void deleteObjects(List<String> objectIds, String token) {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);

        for (String objectId : objectIds) {
            deleteObjectSteps.deleteObject(objectId);
        }
    }

    protected String getObjectHash(String fileName) {
        String res = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] array = Files.readAllBytes(Path.of(fileName));

            res = Base64.encodeBase64String(md.digest(array));
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    protected String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    @DataProvider(name = "files")
    public Object[][] provideFiles() {
        return new Object[][]{
                {TRAINING_FILE},  // upload an image
                {SAMPLE_VIDEO_FILE}    // upload a video
        };
    }
}
