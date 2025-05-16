package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.GetUserInfoResponseDto;
import com.photopixels.api.steps.users.GetUserInfoSteps;
import com.photopixels.api.steps.users.PostChangeUserPasswordSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;
import static com.photopixels.enums.ErrorMessagesEnum.NEW_PASSWORD;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class PostChangeUserPasswordTests extends ApiBaseTest {

    private String token;
    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "TestUser" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

        token = getToken(email, PASSWORD);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Change user password")
    @Description("Successful change of user password")
    @Story("Change User Password")
    @Severity(SeverityLevel.CRITICAL)
    public void changeUserPasswordTest() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "TestUser" + random;
        String email = "testuser" + random + "@test.com";
        String newPassword = "!Test98765";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, newPassword);

        String token = getToken(email, PASSWORD);

        PostChangeUserPasswordSteps postChangeUserPasswordSteps = new PostChangeUserPasswordSteps(token);
        postChangeUserPasswordSteps.changeUserPassword(PASSWORD, newPassword);

        String newToken = getToken(email, newPassword);

        GetUserInfoSteps getUserInfoSteps = new GetUserInfoSteps(newToken);
        GetUserInfoResponseDto getUserInfoResponseDto = getUserInfoSteps.getUserInfo();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getUserInfoResponseDto.getEmail(), email, "User email is not correct");
        softAssert.assertEquals(getUserInfoResponseDto.getClaims().getEmail(), email, "User email in claims is not correct");

        softAssert.assertAll();
    }

    @DataProvider(name = "wrongPasswords")
    public Object[][] getWrongPasswords() {
        return new Object [][] { {"T3st!es", ErrorMessagesEnum.PASSWORD_TOO_SHORT},
                {"Test1234", ErrorMessagesEnum.PASSWORD_REQUIRES_NON_ALPHANUMERIC},
                {"1est!est", ErrorMessagesEnum.PASSWORD_REQUIRES_UPPER},
                {"Test!est", ErrorMessagesEnum.PASSWORD_REQUIRES_DIGIT},
                {null, NEW_PASSWORD} };
    }

    @Test(dataProvider = "wrongPasswords", description = "Change user password with not valid password")
    @Description("Validation of new password requirements during change of user password")
    @Story("Change User Password")
    @Severity(SeverityLevel.NORMAL)
    public void changeUserPasswordWithNotValidNewPasswordTest(String newPassword, ErrorMessagesEnum errorMessage) {
        PostChangeUserPasswordSteps postChangeUserPasswordSteps = new PostChangeUserPasswordSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserPasswordSteps.changeUserPasswordError(PASSWORD, newPassword);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the response for other cases
        if (Objects.requireNonNull(errorMessage) == NEW_PASSWORD) {
            softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE,
                    "Error title is not correct");
            softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST,
                    "Error status is not correct");
        }

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(errorMessage.getKey()),
                errorMessage.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Change user password no old password")
    @Description("Change user password with missing old password")
    @Story("Change User Password")
    @Severity(SeverityLevel.MINOR)
    public void changeUserPasswordNoOldPasswordTest() {
        String newPassword = "NewPassword";

        PostChangeUserPasswordSteps postChangeUserPasswordSteps = new PostChangeUserPasswordSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserPasswordSteps.changeUserPasswordError(null, newPassword);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OLD_PASSWORD.getKey()),
                ErrorMessagesEnum.OLD_PASSWORD.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Change user password invalid old password")
    @Description("Change user password with invalid old password")
    @Story("Change User Password")
    @Severity(SeverityLevel.NORMAL)
    public void changeUserPasswordInvalidOldPasswordTest() {
        String invalidOldPassword = "InvalidOldPassword";
        String newPassword = "NewPassword";

        PostChangeUserPasswordSteps postChangeUserPasswordSteps = new PostChangeUserPasswordSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserPasswordSteps.changeUserPasswordError(invalidOldPassword, newPassword);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.PASSWORD_MISMATCH.getKey()),
                ErrorMessagesEnum.PASSWORD_MISMATCH.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
