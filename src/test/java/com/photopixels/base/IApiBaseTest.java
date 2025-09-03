package com.photopixels.base;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.admin.DeleteUserAdminSteps;
import com.photopixels.api.steps.admin.GetUsersSteps;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.enums.UserRolesEnum;
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
import java.util.NoSuchElementException;

import static com.photopixels.constants.Constants.SAMPLE_VIDEO_FILE;
import static com.photopixels.constants.Constants.TRAINING_FILE;
import static io.restassured.RestAssured.baseURI;

public interface IApiBaseTest extends IBaseTest {

    @BeforeSuite(alwaysRun = true)
    default void initSuiteApi() {
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
    default void prepareUsers() {
        String token = getAdminToken();

        GetUsersSteps getUsersSteps = new GetUsersSteps(token);
        GetUserResponseDto[] getUserResponseDto = getUsersSteps.getUsers();

        if (Arrays.stream(getUserResponseDto).noneMatch(
                u -> u.getEmail().equals(inputData.getUsername()))) {
            registerUser(inputData.getUserFullName(), inputData.getUsername(), inputData.getPassword(), UserRolesEnum.USER);
        }
    }

     default String getUserToken() {
        return getToken(inputData.getUsername(), inputData.getPassword());
    }

    default String getAdminToken() {
        return getToken(inputData.getUsernameAdmin(), inputData.getPasswordAdmin());
    }

    default String getToken(String username, String password) {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

        return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
    }

    default String getUserId(String username) {
        GetUsersSteps getUsersSteps = new GetUsersSteps(getAdminToken());
        GetUserResponseDto[] getUserResponseDtos = getUsersSteps.getUsers();

        return Arrays.stream(getUserResponseDtos)
                .filter(user -> user.getEmail().equals(username))
                .map(GetUserResponseDto::getId)
                .findFirst()
                .orElse("");
    }

    default void deleteRegisteredUsers(Map<String, String> registeredUsers) {
        if (!registeredUsers.isEmpty()) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                String token = getToken(entry.getKey(), entry.getValue());

                DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
                deleteUserSteps.deleteUser(entry.getValue());
            }
        }
    }

    default void deleteRegisteredUsersAdmin(List<String> registeredUsers) {
        if (!registeredUsers.isEmpty()) {
            String token = getAdminToken();

            for (String username : registeredUsers) {
                String userId = getUserId(username);

                if (userId.isEmpty()) {
                    continue;
                }

                DeleteUserAdminSteps deleteUserAdminSteps = new DeleteUserAdminSteps(token);
                deleteUserAdminSteps.deleteUserAdmin(userId);
            }
        }
    }

    default void deleteObjects(List<String> objectIds, String token) {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);

        for (String objectId : objectIds) {
            deleteObjectSteps.deleteObject(objectId);
        }
    }

    default String getObjectHash(String fileName) {
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

    default String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    default void registerUser(String username, String email, String password, UserRolesEnum role) {
        String token = getAdminToken();

        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        postRegisterUserAdminSteps.registerUserAdmin(username, email, password, role);
    }

    @DataProvider(name = "files")
    default Object[][] provideFiles() {
        return new Object[][]{
                {TRAINING_FILE},  // upload an image
                {SAMPLE_VIDEO_FILE}    // upload a video
        };
    }
}
