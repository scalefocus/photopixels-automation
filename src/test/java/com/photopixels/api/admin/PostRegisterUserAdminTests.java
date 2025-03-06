package com.photopixels.api.admin;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.enums.UserRolesEnum;
import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.base.ApiBaseTest;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class PostRegisterUserAdminTests extends ApiBaseTest {

    private String token;
    private String name;
    private String email;
    private String password = "Test12345!";

    private List<String> registeredUsersList = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";

        token = getAdminToken();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsersAdmin(registeredUsersList);
    }

    @DataProvider(name = "userRoles")
    public Object[][] getUserRoles() {
        return new Object [][] { {UserRolesEnum.USER},
                {UserRolesEnum.ADMIN} };
    }

    @Test(description = "Register user by admin", dataProvider = "userRoles")
    @Description("Successful registration of a user by admin")
    @Story("Register User Admin")
    @Severity(SeverityLevel.CRITICAL)
    public void registerUserAdminTest(UserRolesEnum role) {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        postRegisterUserAdminSteps.registerUserAdmin(name, email, password, role);

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

    @Test(dataProvider = "wrongPasswords", description = "Register user by admin not valid password")
    @Description("Validation of password requirements")
    @Story("Register User Admin")
    @Severity(SeverityLevel.NORMAL)
    public void registerUserAdminNotValidPasswordTest(String password, ErrorMessagesEnum errorMessage) {
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        ErrorResponseDto errorResponseDto = postRegisterUserAdminSteps
                .registerUserAdminError(name, email, password, UserRolesEnum.USER);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(errorMessage.getKey()),
                errorMessage.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user by admin no email")
    @Description("Register user by admin with missing email")
    @Story("Register User Admin")
    @Severity(SeverityLevel.MINOR)
    public void registerUserAdminNoEmailTest() {
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        ErrorResponseDto errorResponseDto = postRegisterUserAdminSteps
                .registerUserAdminError(name, null, password, UserRolesEnum.USER);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.EMAIL.getKey()),
                ErrorMessagesEnum.EMAIL.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user by admin no name")
    @Description("Register user by admin with missing name")
    @Story("Register User Admin")
    @Severity(SeverityLevel.MINOR)
    public void registerUserAdminNoNameTest() {
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        ErrorResponseDto errorResponseDto = postRegisterUserAdminSteps
                .registerUserAdminError(null, email, password, UserRolesEnum.USER);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.NAME.getKey()),
                ErrorMessagesEnum.NAME.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Register user by admin duplicate email")
    @Description("Register user by admin with duplicate email")
    @Story("Register User Admin")
    @Severity(SeverityLevel.NORMAL)
    public void registerUserAdminDuplicateUserTest() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test " + random;
        String email = "test" + random + "@test.com";

        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        postRegisterUserAdminSteps.registerUserAdmin(name, email, password, UserRolesEnum.USER);

        registeredUsersList.add(email);

        ErrorResponseDto errorResponseDto = postRegisterUserAdminSteps
                .registerUserAdminError(name, email, password, UserRolesEnum.USER);

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
