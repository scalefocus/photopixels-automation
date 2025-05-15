package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.steps.admin.PostDisableRegistrationSteps;
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

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class PostRegisterUserTests extends ApiBaseTest {

    private String name;
    private String email;

    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "TestUser" + random;
        email = "testuser" + random + "@test.com";
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Register user")
    @Description("Successful registration of a user")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void registerUserTest() {
        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

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
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, null, PASSWORD);

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
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(null, email, PASSWORD);

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
        String name = "Test" + random;
        String email = "test" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, email, PASSWORD);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.DUPLICATE_USER_NAME.getKey()),
                String.format(ErrorMessagesEnum.DUPLICATE_USER_NAME.getErrorMessage(), name), "Error message is not correct");
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.DUPLICATE_EMAIL.getKey()),
                String.format(ErrorMessagesEnum.DUPLICATE_EMAIL.getErrorMessage(), email), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user disabled registration")
    @Description("Register user when registration is disabled")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerUserDisabledRegistrationTest() {
        // Disable registration
        PostDisableRegistrationSteps postDisableRegistrationSteps = new PostDisableRegistrationSteps(getAdminToken());
        postDisableRegistrationSteps.disableRegistration(false);

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        ErrorResponseDto errorResponseDto = postRegisterUserSteps.registerUserError(name, email, PASSWORD);

        // Enable registration
        postDisableRegistrationSteps.disableRegistration(true);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REGISTRATION_IS_DISABLED.getKey()),
                ErrorMessagesEnum.REGISTRATION_IS_DISABLED.getErrorMessage(), "Error message is not correct");
        softAssert.assertAll();
    }

}
