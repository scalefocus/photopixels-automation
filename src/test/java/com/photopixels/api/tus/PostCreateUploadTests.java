package com.photopixels.api.tus;

import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.Constants.VALID_UPLOAD_LENGTH;
import static com.photopixels.constants.Constants.VALID_UPLOAD_METADATA;

@Listeners(StatusTestListener.class)
@Feature("Tus")
public class PostCreateUploadTests extends ApiBaseTest {
    private String token;
    private PostCreateUploadSteps steps;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();
        steps = new PostCreateUploadSteps(token);
    }

    @Test(description = "Successfully create upload")
    @Description("Positive test: Verify that an upload resource is created successfully")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)

    public void createUploadSuccessfully() {

        //TODO:Replace hardcoded uploadMetadata and uploadLength with dynamically generated values (e.g. encode fileName, extension, etc.)

        Response response = steps.createUpload(VALID_UPLOAD_METADATA, VALID_UPLOAD_LENGTH);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(response.getHeaders().hasHeaderWithName("Location"), "Missing 'Location' header in response");

        softAssert.assertAll();
    }

    @Test(description = "Create upload with missing metadata")
    @Description("Negative test: Try to create upload without Upload-Metadata header")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)
    public void createUploadWithoutMetadata() {
        String uploadMetadata = "";

        String responseBody = steps.createUploadError(uploadMetadata, VALID_UPLOAD_LENGTH);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(responseBody.contains("Upload-Metadata"), "Expected error message to reference 'Upload-Metadata'");

        softAssert.assertAll();
    }

    @Test(description = "Create upload with missing length")
    @Description("Negative test: Try to create upload without Upload-Length header")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)
    public void createUploadWithoutLength() {
        String uploadLength = "";

        String responseBody = steps.createUploadError(VALID_UPLOAD_METADATA, uploadLength);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(responseBody.contains("Upload-Length"), "Expected error message to reference 'Upload-Length'");

        softAssert.assertAll();
    }
}
