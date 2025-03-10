package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.Constants.PASSWORD;
import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class DeleteUserTests extends ApiBaseTest {

    private String name;
    private String email;
    private String token;

    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

        token = getToken(email, PASSWORD);
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
       deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Delete user")
    @Description("Successful deletion of a user")
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    public void deleteUserTest() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        String token = getToken(email, PASSWORD);

        DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
        deleteUserSteps.deleteUser(PASSWORD);

        // No response is returned
    }

    @Test(description = "Delete user with incorrect password")
    @Description("Validation of delete user with incorrect password")
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    public void deleteUserWrongPasswordTest() {
        String incorrectPassword = "TempT3mp!";

        DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
        deleteUserSteps.deleteUserErrorForbidden(incorrectPassword);

        // No response body is returned
    }

    @Test(description = "Delete user with missing password")
    @Description("Validation of delete user with missing password")
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    public void deleteUserNoPasswordTest() {
        DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
        ErrorResponseDto errorResponseDto = deleteUserSteps.deleteUserError(null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.PASSWORD.getKey()),
                ErrorMessagesEnum.PASSWORD.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }
}
