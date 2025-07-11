package com.photopixels.api.status;

import com.photopixels.api.steps.status.GetLogsSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Listeners(StatusTestListener.class)
@Feature("Status")
public class GetLogsTests implements IApiBaseTest {

    @Test(description = "Get logs admin user")
    @Description("Get logs with admin user")
    @Story("Get logs")
    @Severity(SeverityLevel.NORMAL)
    public void getLogsTest() {
        String adminToken = getAdminToken();

        GetLogsSteps getLogsSteps = new GetLogsSteps(adminToken);
        String logs = getLogsSteps.getLogs();

        LocalDateTime objectDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        String expectedDate = objectDateTime.toString().substring(0, 10);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(logs.contains(expectedDate),
                "Expected today's date (" + expectedDate + ") not found in logs");

        softAssert.assertAll();
    }

    @Test(description = "Get logs regular user")
    @Description("Get logs with regular user")
    @Story("Get logs")
    @Severity(SeverityLevel.NORMAL)
    public void getLogsRegularUserTest() {
        String userToken = getUserToken();

        GetLogsSteps getLogsSteps = new GetLogsSteps(userToken);
        getLogsSteps.getLogsWithError(HttpStatus.SC_FORBIDDEN);

        // No response body is returned
    }

    @Test(description = "Get logs unauthorized user")
    @Description("Get logs with unauthorized user")
    @Story("Get logs")
    @Severity(SeverityLevel.NORMAL)
    public void getLogsUnauthorizedUserTest() {
        GetLogsSteps getLogsSteps = new GetLogsSteps(null);
        getLogsSteps.getLogsWithError(HttpStatus.SC_UNAUTHORIZED);

        // No response body is returned
    }
}
