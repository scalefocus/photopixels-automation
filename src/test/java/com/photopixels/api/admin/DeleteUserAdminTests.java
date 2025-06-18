package com.photopixels.api.admin;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.steps.admin.DeleteUserAdminSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.UUID;

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class DeleteUserAdminTests extends ApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
    }

    @Test(description = "Delete user by admin")
    @Description("Successful deletion of a user by admin")
    @Story("Delete User by Admin")
    @Severity(SeverityLevel.NORMAL)
    public void deleteUserAdminTest() {
        String random = RandomStringUtils.randomNumeric(6);
        String name = "TestUser" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        String userId = getUserId(email);

        DeleteUserAdminSteps deleteUserAdminSteps = new DeleteUserAdminSteps(token);
        deleteUserAdminSteps.deleteUserAdmin(userId);

        // No response is returned
    }

    @Test(description = "Delete user by admin with not existing id")
    @Description("Validation of delete user by admin with not existing id")
    @Story("Delete User by Admin")
    @Severity(SeverityLevel.MINOR)
    public void deleteUserAdminNotExistingIdTest() {
        String notExistingId = UUID.randomUUID().toString();

        DeleteUserAdminSteps deleteUserAdminSteps = new DeleteUserAdminSteps(token);
        ErrorResponseDto errorResponseDto = deleteUserAdminSteps.deleteUserAdminError(notExistingId);

        SoftAssert softAssert = new SoftAssert();

        // TODO: Check if this is the expected response
        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }
}
