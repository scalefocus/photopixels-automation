package com.photopixels.api.users;

import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.steps.users.*;
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

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class PostForgotUserPasswordTests extends ApiBaseTest {

    private String email;
    private String password = "Test12345!";
    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, password);

        registeredUsersList.put(email, password);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Forgot user password")
    @Description("Successful reset of forgotten user password")
    @Story("Forgot User Password")
    @Severity(SeverityLevel.CRITICAL)
    public void forgotUserPasswordTest() {
        PostForgotUserPasswordSteps postForgotUserPasswordSteps = new PostForgotUserPasswordSteps();
        postForgotUserPasswordSteps.forgotUserPassword(email);

        // Empty response is returned
    }

    @Test(description = "Forgot user password no email")
    @Description("Forgot user password with missing email")
    @Story("Forgot User Password")
    @Severity(SeverityLevel.MINOR)
    public void forgotUserPasswordNoEmailTest() {
        PostForgotUserPasswordSteps postForgotUserPasswordSteps = new PostForgotUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postForgotUserPasswordSteps.forgotUserPasswordError(null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Forgot user password invalid email")
    @Description("Forgot user password with invalid email")
    @Story("Forgot User Password")
    @Severity(SeverityLevel.NORMAL)
    public void forgotUserPasswordInvalidEmailTest() {
        String invalidEmail = "InvalidEmail";

        PostForgotUserPasswordSteps postForgotUserPasswordSteps = new PostForgotUserPasswordSteps();
        ErrorResponseDto errorResponseDto = postForgotUserPasswordSteps.forgotUserPasswordError(invalidEmail);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.PASSWORD_MISMATCH.getKey()),
                ErrorMessagesEnum.PASSWORD_MISMATCH.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
