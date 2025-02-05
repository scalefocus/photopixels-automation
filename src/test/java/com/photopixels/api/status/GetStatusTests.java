package com.photopixels.api.status;

import com.photopixels.api.base.BaseTest;
import com.photopixels.api.dtos.status.GetStatusResponseDto;
import com.photopixels.api.steps.status.GetStatusSteps;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Status")
public class GetStatusTests extends BaseTest {

    @Test(description = "Get status")
    @Description("Get status")
    @Story("Get status")
    @Severity(SeverityLevel.NORMAL)
    public void getStatusTest() {
        GetStatusSteps getStatusSteps = new GetStatusSteps();
        GetStatusResponseDto getStatusResponseDto = getStatusSteps.getStatus();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getStatusResponseDto, "Status is not returned!");

        softAssert.assertAll();
    }

}
