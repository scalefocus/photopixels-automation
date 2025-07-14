package com.photopixels.api.admin;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.steps.admin.GetUsersSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class GetUsersTests implements IApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
    }

    @Test(description = "Get users")
    @Description("Get users list")
    @Story("Get Users")
    @Severity(SeverityLevel.NORMAL)
    public void getUsersTest() {
        GetUsersSteps getUsersSteps = new GetUsersSteps(token);
        GetUserResponseDto[] getUserResponseDto = getUsersSteps.getUsers();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getUserResponseDto.length > 0, "Users are not returned!");

        for (GetUserResponseDto user : getUserResponseDto) {
            softAssert.assertNotNull(user.getId(), "User id is missing");
            softAssert.assertNotNull(user.getName(), "User name is missing");
            softAssert.assertNotNull(user.getEmail(), "User email is missing");
            softAssert.assertNotNull(user.getQuota(), "Quota is not returned!");
            softAssert.assertNotNull(user.getUsedQuota(), "Used quota is not returned!");
        }

        softAssert.assertAll();
    }
}
