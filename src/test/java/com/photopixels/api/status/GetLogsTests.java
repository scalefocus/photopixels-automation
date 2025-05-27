package com.photopixels.api.status;

import com.photopixels.api.steps.status.GetLogsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Listeners(StatusTestListener.class)
@Feature("Status")
public class GetLogsTests extends ApiBaseTest {

    @Test(description = "Get logs")
    @Description("Get logs")
    @Story("Get logs")
    @Severity(SeverityLevel.NORMAL)
    public void getLogsTest(){

        // TODO: Determine if authentication token is needed and which type (user/admin).
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/73");

        GetLogsSteps getLogsSteps = new GetLogsSteps();
        String logs = getLogsSteps.getLogs();

        LocalDateTime objectDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        String expectedDate = objectDateTime.toString().substring(0, 10);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(logs.contains("BearerToken signed in"), "Expected successful authentication message is missing");
        softAssert.assertTrue(logs.contains("BearerToken was not authenticated"), "Missing 'BearerToken' message");
        softAssert.assertTrue(logs.contains("HTTP/1.1 GET"), "Missing HTTP request method");
        softAssert.assertTrue(logs.contains(expectedDate), "Expected today's date (" + expectedDate + ") not found in logs");
        softAssert.assertTrue(logs.contains("401"), "Missing HTTP status code 401");

        softAssert.assertAll();
    }
}
