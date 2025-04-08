package com.photopixels.api.status;

import com.photopixels.api.steps.status.GetLogsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Listeners(StatusTestListener.class)
@Feature("Logs")
public class GetLogsTests extends ApiBaseTest {

    @Test(description = "Get logs")
    @Description("Get logs")
    @Story("Get logs")
    @Severity(SeverityLevel.NORMAL)

    public void shouldReturnLogsWithExpectedMessages(){
        String token = getAdminToken();

        GetLogsSteps getLogsSteps = new GetLogsSteps(token);
        Response response = getLogsSteps.getLogs();
        String logs = response.getBody().asString();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(response.getStatusCode(), 200,"Expected status code 200");
        softAssert.assertTrue(logs.contains("BearerToken signed in"), "Expected successful authentication message is missing");
        softAssert.assertTrue(logs.contains("BearerToken was not authenticated"), "Missing 'BearerToken' message");
        softAssert.assertTrue(logs.contains("HTTP/1.1 GET"), "Missing HTTP request method");
        softAssert.assertTrue(logs.contains("2025-04-08"), "Expected date not found in logs");
        softAssert.assertTrue(logs.contains("401"), "Missing HTTP status code 401");

        softAssert.assertAll();
    }
}
