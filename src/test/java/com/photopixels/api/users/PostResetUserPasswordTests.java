package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.api.steps.users.PostResetUserPasswordSteps;
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
public class PostResetUserPasswordTests extends ApiBaseTest {

    private String email;
    private String code;

    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        email = "testuser" + random + "@test.com";
        code = RandomStringUtils.randomNumeric(6);

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Reset user password")
    @Description("Successful reset of user password")
    @Story("Reset User Password")
    @Severity(SeverityLevel.CRITICAL)
    public void resetUserPasswordTest() {

        // TODO: call /user/forgotpassword to get valid code from the sent mail

        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        postResetUserPasswordSteps.resetUserPassword(code, PASSWORD, email);

        // Empty response is returned
    }

    @Test(description = "Reset user password no password")
    @Description("Reset user password with missing  password")
    @Story("Reset User Password")
    @Severity(SeverityLevel.MINOR)
    public void resetUserPasswordNoPasswordTest() {
        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postResetUserPasswordSteps.resetUserPasswordError(code, null, email);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.PASSWORD.getKey()),
                ErrorMessagesEnum.PASSWORD.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Reset user password no email")
    @Description("Reset user password with missing email")
    @Story("Reset User Password")
    @Severity(SeverityLevel.MINOR)
    public void resetUserPasswordNoEmailTest() {
        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postResetUserPasswordSteps.resetUserPasswordError(code, PASSWORD, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Reset user password no code")
    @Description("Reset user password with missing code")
    @Story("Reset User Password")
    @Severity(SeverityLevel.MINOR)
    public void resetUserPasswordNoCodeTest() {
        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postResetUserPasswordSteps.resetUserPasswordError(null, PASSWORD, email);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.CODE.getKey()),
                ErrorMessagesEnum.CODE.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Reset user password invalid email")
    @Description("Reset user password with invalid email")
    @Story("Reset User Password")
    @Severity(SeverityLevel.NORMAL)
    public void resetUserPasswordInvalidEmailTest() {
        String email = "InvalidEmail";

        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postResetUserPasswordSteps.resetUserPasswordError(code, PASSWORD, email);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.USER_NOT_FOUND.getKey()),
                ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Reset user password invalid code")
    @Description("Reset user password with invalid code")
    @Story("Reset User Password")
    @Severity(SeverityLevel.NORMAL)
    public void resetUserPasswordInvalidCodeTest() {
        String invalidCode = "InvalidCode";

        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postResetUserPasswordSteps.resetUserPasswordError(invalidCode, PASSWORD, email);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.INVALID_CODE.getKey()),
                ErrorMessagesEnum.INVALID_CODE.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
