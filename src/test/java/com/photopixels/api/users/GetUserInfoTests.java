package com.photopixels.api.users;

import com.photopixels.api.base.BaseTest;
import com.photopixels.api.dtos.users.GetUserInfoResponseDto;
import com.photopixels.api.steps.users.GetUserInfoSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Users")
public class GetUserInfoTests extends BaseTest {

    private String token;
    private String email;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        email = inputData.getUsername();
        token = getToken(email, inputData.getPassword());
    }

    @Test(description = "Get user info")
    @Description("Get user information")
    @Story("Get User Info")
    @Severity(SeverityLevel.NORMAL)
    public void getUserInfoTest() {
        GetUserInfoSteps getUserInfoSteps = new GetUserInfoSteps(token);
        GetUserInfoResponseDto getUserInfoResponseDto = getUserInfoSteps.getUserInfo();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getUserInfoResponseDto.getEmail(), email, "User email is not correct");
        softAssert.assertNotNull(getUserInfoResponseDto.getQuota(), "Quota is not returned!");
        softAssert.assertNotNull(getUserInfoResponseDto.getUsedQuota(), "Used quota is not returned!");

        softAssert.assertEquals(getUserInfoResponseDto.getClaims().getEmail(), email,
                "User email in claims is not correct");

        softAssert.assertAll();
    }
}
