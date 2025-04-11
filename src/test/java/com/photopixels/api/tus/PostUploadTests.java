package com.photopixels.api.tus;

import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.ApiBaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.Constants.VALID_UPLOAD_LENGTH;
import static com.photopixels.constants.Constants.VALID_UPLOAD_METADATA;

@Feature("Tus")
public class PostUploadTests extends ApiBaseTest {
    private String token;

    @Test(description = "Successfully create upload")
    @Description("Positive test: Verify that an upload resource is created successfully")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)

    public void createUploadSuccessfully() {
        token = getUserToken();

        //TODO:Replace hardcoded uploadMetadata and uploadLength with dynamically generated values (e.g. encode fileName, extension, etc.)

        String uploadMetadata = VALID_UPLOAD_METADATA;
        String uploadLength = VALID_UPLOAD_LENGTH;

        PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
        Response response = steps.createUpload(uploadMetadata, uploadLength);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(response.getStatusCode(), 201, "Expected status 201 Created");
        softAssert.assertTrue(response.getHeaders().hasHeaderWithName("Location"), "Missing 'Location' header in response");

        softAssert.assertAll();
    }

    @Test(description = "Create upload with missing metadata")
    @Description("Negative test: Try to create upload without Upload-Metadata header")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)
    public void createUploadWithoutMetadata() {
        token = getUserToken();
        String uploadMetadata = "";
        String uploadLength = VALID_UPLOAD_LENGTH;

        PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
        Response response = steps.createUploadError(uploadMetadata, uploadLength, 400);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status 400 Bad Request");
        softAssert.assertTrue(response.asString().contains("Upload-Metadata"), "Expected error message to reference 'Upload-Metadata'");

        softAssert.assertAll();
    }

    @Test(description = "Create upload with missing length")
    @Description("Negative test: Try to create upload without Upload-Length header")
    @Story("Create Upload")
    @Severity(SeverityLevel.NORMAL)
    public void createUploadWithoutLength() {
        token = getUserToken();
        String uploadMetadata = VALID_UPLOAD_METADATA;
        String uploadLength = "";

        PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
        Response response = steps.createUploadError(uploadMetadata, uploadLength, 400);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status 400 Bad Request");
        softAssert.assertTrue(response.asString().contains("Upload-Length"), "Expected error message to reference 'Upload-Length'");

        softAssert.assertAll();
    }
}
