package com.photopixels.api.admin;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.steps.admin.GetUserSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.enums.UserRolesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.UUID;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class GetUserTests implements IApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
    }

    @DataProvider(name = "users")
    public Object[][] getUsers() {
        return new Object [][] { {inputData.getUsername(), UserRolesEnum.USER},
                {inputData.getUsernameAdmin(), UserRolesEnum.ADMIN} };
    }

    @Test(description = "Get user", dataProvider = "users")
    @Description("Get user by id")
    @Story("Get User")
    @Severity(SeverityLevel.NORMAL)
    public void getUserTest(String username, UserRolesEnum role) {
        String userId = getUserId(username);

        GetUserSteps getUserSteps = new GetUserSteps(token);
        GetUserResponseDto getUserResponseDto = getUserSteps.getUser(userId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getUserResponseDto.getId(), userId, "User id is not correct");
        softAssert.assertNotNull(getUserResponseDto.getName(), "User name is missing");
        softAssert.assertEquals(getUserResponseDto.getEmail(), username, "User email is not correct");
        softAssert.assertNotNull(getUserResponseDto.getUserName(), "User name is missing");
        softAssert.assertNotNull(getUserResponseDto.getQuota(), "Quota is not returned!");
        softAssert.assertNotNull(getUserResponseDto.getUsedQuota(), "Used quota is not returned!");
        softAssert.assertEquals(getUserResponseDto.getRole(), role.getValue(), "User role is not  correct");

        softAssert.assertAll();
    }

    @Test(description = "Get user not existing id")
    @Description("Get user by id with not existing user id")
    @Story("Get User")
    @Severity(SeverityLevel.MINOR)
    public void getUserNotExistingId() {
        String notExistingId = UUID.randomUUID().toString();

        GetUserSteps getUserSteps = new GetUserSteps(token);
        getUserSteps.getUserNoContent(notExistingId);

        // No response body in case the user is not found
    }
}
