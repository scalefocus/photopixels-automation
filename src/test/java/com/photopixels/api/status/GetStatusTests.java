package com.photopixels.api.status;

import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.api.dtos.status.GetStatusResponseDto;
import com.photopixels.api.steps.status.GetStatusSteps;
import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(StatusTestListener.class)
@Feature("Status")
public class GetStatusTests extends ApiBaseTest {

    @Test(description = "Get status")
    @Description("Get status")
    @Story("Get status")
    @Severity(SeverityLevel.NORMAL)
    public void getStatusTest() {
        GetStatusSteps getStatusSteps = new GetStatusSteps();
        GetStatusResponseDto getStatusResponseDto = getStatusSteps.getStatus();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getStatusResponseDto, "Status is not returned!");
        softAssert.assertNotNull(getStatusResponseDto.getRegistration(), "Registration is not returned!");
        softAssert.assertNotNull(getStatusResponseDto.getServerVersion(), "Server version is not returned!");

        softAssert.assertAll();
    }

}
