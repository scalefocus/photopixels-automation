package com.photopixels.api.admin;

import com.photopixels.api.dtos.status.GetStatusResponseDto;
import com.photopixels.api.steps.admin.PostDisableRegistrationSteps;
import com.photopixels.api.steps.status.GetStatusSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class PostDisableRegistrationTests extends ApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        // Enable the registration
        PostDisableRegistrationSteps postDisableRegistrationSteps = new PostDisableRegistrationSteps(token);
        postDisableRegistrationSteps.disableRegistration(true);
    }

    @Test(description = "Disable registration successfully")
    @Description("Disable registration successfully with admin user")
    @Story("Disable Registration")
    @Severity(SeverityLevel.NORMAL)
    public void disableRegistrationTest() {
        PostDisableRegistrationSteps postDisableRegistrationSteps = new PostDisableRegistrationSteps(token);
        postDisableRegistrationSteps.disableRegistration(false);

        GetStatusSteps getStatusSteps = new GetStatusSteps();
        GetStatusResponseDto getStatusResponseDto = getStatusSteps.getStatus();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(getStatusResponseDto.getRegistration(), "Registration is not disabled!");

        softAssert.assertAll();
    }

    @Test(description = "Disable registration not admin user")
    @Description("Disable registration successfully with non admin user")
    @Story("Disable Registration")
    @Severity(SeverityLevel.MINOR)
    public void disableRegistrationNotAdminUserTest() {
        String token = getUserToken();

        PostDisableRegistrationSteps postDisableRegistrationSteps = new PostDisableRegistrationSteps(token);
        postDisableRegistrationSteps.disableRegistrationExpectingForbidden(false);

        GetStatusSteps getStatusSteps = new GetStatusSteps();
        GetStatusResponseDto getStatusResponseDto = getStatusSteps.getStatus();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getStatusResponseDto.getRegistration(), "Registration is disabled!");

        softAssert.assertAll();
    }

}
