package com.photopixels.api.admin;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.api.steps.admin.PostResetPasswordAdminSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.Constants.PASSWORD;
import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class PostResetPasswordAdminTests extends ApiBaseTest {

    private String token;
    private String email;

    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();

        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        // TODO: Could be changed with admin deletion of user and creation of only 1 user will be needed
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Reset password admin")
    @Description("Successful reset of password by admin")
    @Story("Reset Password by Admin")
    @Severity(SeverityLevel.CRITICAL)
    public void resetPasswordAdminTest() {
        String newPassword = "Test12345!";
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        PostResetPasswordAdminSteps postResetPasswordAdminSteps = new PostResetPasswordAdminSteps(token);
        postResetPasswordAdminSteps.resetPasswordAdmin(newPassword, email);

        registeredUsersList.put(email, newPassword);

        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(email, newPassword);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(loginResponseDto.getTokenType(), "Bearer", "Token type is not correct");
        softAssert.assertNotNull(loginResponseDto.getAccessToken(), "Access token is missing");
        softAssert.assertNotNull(loginResponseDto.getRefreshToken(), "Refresh token is missing");
        softAssert.assertNotNull(loginResponseDto.getUserId(), "User id is missing");

        softAssert.assertAll();
    }

    @Test(description = "Reset password admin no email")
    @Description("Reset password by admin with missing email")
    @Story("Reset Password by Admin")
    @Severity(SeverityLevel.MINOR)
    public void resetPasswordAdminNoEmailTest() {
        PostResetPasswordAdminSteps postResetPasswordAdminSteps = new PostResetPasswordAdminSteps(token);
        ErrorResponseDto errorResponseDto = postResetPasswordAdminSteps.resetPasswordAdminError(PASSWORD, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Reset password admin invalid email")
    @Description("Reset password by admin with invalid email")
    @Story("Reset Password by Admin")
    @Severity(SeverityLevel.NORMAL)
    public void resetPasswordAdminInvalidEmailTest() {
        String email = "InvalidEmail";

        PostResetPasswordAdminSteps postResetPasswordAdminSteps = new PostResetPasswordAdminSteps(token);
        ErrorResponseDto errorResponseDto = postResetPasswordAdminSteps.resetPasswordAdminError(PASSWORD, email);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.USER_NOT_FOUND.getKey()),
                ErrorMessagesEnum.USER_NOT_FOUND.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }


    @DataProvider(name = "wrongPasswords")
    public Object[][] getWrongPasswords() {
        return new Object [][] { {"T3st!es", ErrorMessagesEnum.PASSWORD_TOO_SHORT},
                {"Test1234", ErrorMessagesEnum.PASSWORD_REQUIRES_NON_ALPHANUMERIC},
                {"1est!est", ErrorMessagesEnum.PASSWORD_REQUIRES_UPPER},
                {"Test!est", ErrorMessagesEnum.PASSWORD_REQUIRES_DIGIT},
                {null, ErrorMessagesEnum.PASSWORD} };
    }

    @Test(dataProvider = "wrongPasswords", description = "Reset password admin invalid password")
    @Description("Reset password by admin with invalid password")
    @Story("Reset Password by Admin")
    @Severity(SeverityLevel.NORMAL)
    public void resetPasswordAdminInvalidPasswordTest(String password, ErrorMessagesEnum errorMessage) {
        PostResetPasswordAdminSteps postResetPasswordAdminSteps = new PostResetPasswordAdminSteps(token);
        ErrorResponseDto errorResponseDto = postResetPasswordAdminSteps.resetPasswordAdminError(password, email);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(errorMessage.getKey()),
                errorMessage.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
