package com.photopixels.api.users;

import com.photopixels.api.base.BaseTest;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Feature("Users")
public class PostRegisterUserTests extends BaseTest {

    private String name;
    private String email;
    private String password = "Test12345!";
    private List<String> registeredUsersList = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";
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

    @Test(description = "Register user")
    @Description("Successful registration of a user")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void registerUserTest() {
        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, password);

        registeredUsersList.add(email);

        // No response body is returned
    }

    @DataProvider(name = "wrongPasswords")
    public Object[][] getWrongPasswords() {
        return new Object [][] { {"T3st!es", ErrorMessagesEnum.PASSWORD_TOO_SHORT},
                {"Test1234", ErrorMessagesEnum.PASSWORD_REQUIRES_NON_ALPHANUMERIC},
                {"1est!est", ErrorMessagesEnum.PASSWORD_REQUIRES_UPPER},
                {"Test!est", ErrorMessagesEnum.PASSWORD_REQUIRES_DIGIT},
                {null, ErrorMessagesEnum.PASSWORD} };
    }

    @Test(dataProvider = "wrongPasswords", description = "Register user not valid password")
    @Description("Validation of password requirements")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerUserNotValidPasswordTest(String password, ErrorMessagesEnum errorMessage) {
        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, email, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(errorMessage.getKey()),
                errorMessage.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user no email")
    @Description("Register user with missing email")
    @Story("Register User")
    @Severity(SeverityLevel.MINOR)
    public void registerUserNoEmailTest() {
        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, null, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user no name")
    @Description("Register user with missing name")
    @Story("Register User")
    @Severity(SeverityLevel.MINOR)
    public void registerUserNoNameTest() {
        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(null, email, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.NAME.getKey()),
                ErrorMessagesEnum.NAME.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user duplicate email")
    @Description("Register user with duplicate email")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerUserDuplicateUserTest() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test " + random;
        String email = "test" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, password);

        registeredUsersList.add(email);

        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, email, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.DUPLICATE_USER_NAME.getKey()),
                String.format(ErrorMessagesEnum.DUPLICATE_USER_NAME.getErrorMessage(), email), "Error message is not correct");
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.DUPLICATE_EMAIL.getKey()),
                String.format(ErrorMessagesEnum.DUPLICATE_EMAIL.getErrorMessage(), email), "Error message is not correct");

        softAssert.assertAll();
    }
}
