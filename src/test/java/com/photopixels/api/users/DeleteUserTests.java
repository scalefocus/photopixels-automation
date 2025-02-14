package com.photopixels.api.users;

import com.photopixels.api.base.BaseTest;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Feature("Users")
public class DeleteUserTests extends BaseTest {

    private String name;
    private String email;
    private String token;
    private String password = "Test12345!";

    private List<String> registeredUsersList = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, password);

        token = getToken(email, password);
    }


    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (!registeredUsersList.isEmpty()) {
            for (String user : registeredUsersList) {
                String token = getToken(user, password);

                DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
                deleteUserSteps.deleteUser(password);
            }
        }
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
        postRegisterUserSteps.registerUser(name, email, password);

        String token = getToken(email, password);

        DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
        deleteUserSteps.deleteUser(password);

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
