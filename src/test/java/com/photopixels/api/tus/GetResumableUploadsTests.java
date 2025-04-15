package com.photopixels.api.tus;

import com.photopixels.api.steps.tus.GetResumableUploadsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(StatusTestListener.class)
@Feature("Tus")
public class GetResumableUploadsTests extends ApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
    }

    @Test(description = "Successfully get resumable uploads")
    @Description("Positive test: Verify that the resumable uploads are returned successfully")
    @Story("Get Resumable Uploads")
    @Severity(SeverityLevel.NORMAL)
    public void getResumableUploadsSuccessfully() {

        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(token);
        String responseBody = getResumableUploadsSteps.getResumableUploads();


        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(responseBody, "Expected non-null response body");
        softAssert.assertTrue(responseBody.contains("userUploads"), "Response body should contain 'userUploads'");

        softAssert.assertAll();
    }

      @Test(description = "Get resumable uploads without token")
      @Description("Negative test: Access resumable uploads endpoint without authentication")
      @Story("Get Resumable Uploads")
      @Severity(SeverityLevel.NORMAL)
      public void getResumableUploadsWithoutToken() {

        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(null);
        Response responseBody = getResumableUploadsSteps.getResumableUploadsError();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(responseBody, "Expected non-null response body");
        softAssert.assertEquals(responseBody.getHeader("Server"), "nginx", "Expected 'Server: nginx'");

        softAssert.assertAll();
    }
}
