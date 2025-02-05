package com.photopixels.api.users;

import com.photopixels.api.base.BaseTest;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.steps.users.PostLoginSteps;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Feature("Users")
public class PostLoginTests extends BaseTest {

    private String email;
    private String password;

    @BeforeClass (alwaysRun = true)
    public void setup() {
        email = inputData.getUsername();
        password = inputData.getPassword();
    }

    @Test(description = "Successful login")
    @Description("Successful login with valid credentials")
    @Story("Login User")
    @Severity(SeverityLevel.CRITICAL)
    public void loginUserTest() {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(email, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(loginResponseDto.getTokenType(), "Bearer", "Token type is not correct");
        softAssert.assertNotNull(loginResponseDto.getAccessToken(), "Access token is missing");
        softAssert.assertNotNull(loginResponseDto.getRefreshToken(), "Refresh token is missing");

        softAssert.assertAll();
    }

    @Test(description = "Login wrong password")
    @Description("Login with existing user and wrong password")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginWrongPasswordTest() {
        String password = "TempT3mp";

        PostLoginSteps postLoginSteps = new PostLoginSteps();
        postLoginSteps.loginErrorForbidden(email, password);

        // No response is returned
    }

    @Test(description = "Login no email")
    @Description("Login with missing email")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginNoEmailTest() {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        ErrorResponseDto errorResponseDto = postLoginSteps.loginError(null, password);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Login no password")
    @Description("Login with missing password")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginNoPasswordTest() {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        ErrorResponseDto errorResponseDto = postLoginSteps.loginError(email, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.PASSWORD.getKey()),
                ErrorMessagesEnum.PASSWORD.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Login not registered user")
    @Description("Login with not registered")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginNotRegisteredUserTest() {
        String email = "test" + RandomStringUtils.randomNumeric(6) + "@test.com";

        PostLoginSteps postLoginSteps = new PostLoginSteps();
        postLoginSteps.loginErrorForbidden(email, password);

        // No response body is returned
    }
}
