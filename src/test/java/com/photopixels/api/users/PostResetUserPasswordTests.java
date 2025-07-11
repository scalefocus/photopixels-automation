package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.users.PostForgotUserPasswordSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.api.steps.users.PostResetUserPasswordSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.helpers.DriverUtils;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.web.pages.email.EmailPage;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class PostResetUserPasswordTests implements IApiBaseTest {

    private String email;
    private String code;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        // Using predefined email to get the reset code from the inbox
        email = "Romer1977@cuvox.de";
        code = RandomStringUtils.randomNumeric(6);
    }

    @Test(description = "Reset user password")
    @Description("Successful reset of user password")
    @Story("Reset User Password")
    @Severity(SeverityLevel.CRITICAL)
    public void resetUserPasswordTest() {
        String newPassword = "Test12345!";

        PostForgotUserPasswordSteps postForgotUserPasswordSteps = new PostForgotUserPasswordSteps();
        postForgotUserPasswordSteps.forgotUserPassword(email);

        String code = getCodeForReset();

        PostResetUserPasswordSteps postResetUserPasswordSteps = new PostResetUserPasswordSteps();
        postResetUserPasswordSteps.resetUserPassword(code, newPassword, email);

        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(email, newPassword);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(loginResponseDto.getTokenType(), "Bearer", "Token type is not correct");
        softAssert.assertNotNull(loginResponseDto.getAccessToken(), "Access token is missing");
        softAssert.assertNotNull(loginResponseDto.getRefreshToken(), "Refresh token is missing");
        softAssert.assertNotNull(loginResponseDto.getUserId(), "User id is missing");

        softAssert.assertAll();
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

    // TODO: Find better solution for the web part in the api tests
    private String getCodeForReset() {
        WebDriver driver = null;

        // Sleep is needed to let the code be sent to the email
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // No need to do anything here
        }
        try {
            driver = new DriverUtils().getDriver();

            driver.get(configProperties.getProperty("emailUrl"));

            EmailPage emailPage = new EmailPage(driver);
            return emailPage.getCodeFromEmail();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
