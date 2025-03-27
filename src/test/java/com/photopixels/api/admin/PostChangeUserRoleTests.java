package com.photopixels.api.admin;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.steps.admin.GetUserSteps;
import com.photopixels.api.steps.admin.PostChangeUserRoleSteps;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.enums.UserRolesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class PostChangeUserRoleTests extends ApiBaseTest {

    private String token;
    private String userId;
    private UserRolesEnum role = UserRolesEnum.USER;;

    private List<String> registeredUsersList = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();

        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        postRegisterUserAdminSteps.registerUserAdmin(name, email, PASSWORD, role);

        registeredUsersList.add(email);

        userId = getUserId(email);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsersAdmin(registeredUsersList);
    }

    @DataProvider(name = "roles")
    public Object[][] getRoles() {
        return new Object [][] { {UserRolesEnum.ADMIN, UserRolesEnum.USER},
                {UserRolesEnum.USER, UserRolesEnum.ADMIN} };
    }

    @Test(description = "Change user role", dataProvider = "roles")
    @Description("Change user role successfully")
    @Story("Change User Role")
    @Severity(SeverityLevel.NORMAL)
    public void changeUserRoleTest(UserRolesEnum originalRole, UserRolesEnum expectedRole) {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "Test User" + random;
        String email = "testuser" + random + "@test.com";
        String password = "Test12345!";

        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
        postRegisterUserAdminSteps.registerUserAdmin(name, email, password, originalRole);

        registeredUsersList.add(email);

        String userId = getUserId(email);

        PostChangeUserRoleSteps postChangeUserRoleSteps = new PostChangeUserRoleSteps(token);
        postChangeUserRoleSteps.changeUserRole(userId, expectedRole);

        GetUserSteps getUserSteps = new GetUserSteps(token);
        GetUserResponseDto getUserResponseDto = getUserSteps.getUser(userId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getUserResponseDto.getRole(), expectedRole.getValue(),
                "User role is not changed!");

        softAssert.assertAll();
    }

    @Test(description = "Change user role no user id")
    @Description("Change user role with missing user id")
    @Story("Change User Role")
    @Severity(SeverityLevel.MINOR)
    public void changeUserRoleNoUserIdTest() {
        PostChangeUserRoleSteps postChangeUserRoleSteps = new PostChangeUserRoleSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserRoleSteps
                .changeUserRoleError(null, role, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        // TODO: Check if this error message is the expected one
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REQUEST.getKey()),
                ErrorMessagesEnum.REQUEST.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Change user role not existing user id")
    @Description("Change user role with not existing user id")
    @Story("Change User Role")
    @Severity(SeverityLevel.MINOR)
    public void changeUserRoleNotExistingUserIdTest() {
        String notExistingId = UUID.randomUUID().toString();

        PostChangeUserRoleSteps postChangeUserRoleSteps = new PostChangeUserRoleSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserRoleSteps
                .changeUserRoleError(notExistingId, role, HttpStatus.SC_NOT_FOUND);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Change user role no role")
    @Description("Change user role with missing role")
    @Story("Change User Role")
    @Severity(SeverityLevel.MINOR)
    public void changeUserRoleNoRoleTest() {
        PostChangeUserRoleSteps postChangeUserRoleSteps = new PostChangeUserRoleSteps(token);
        ErrorResponseDto errorResponseDto = postChangeUserRoleSteps
                .changeUserRoleError(userId, null, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        // TODO: Check if this error message is the expected one
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REQUEST.getKey()),
                ErrorMessagesEnum.REQUEST.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
