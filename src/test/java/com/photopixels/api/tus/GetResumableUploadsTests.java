package com.photopixels.api.tus;

import com.photopixels.api.dtos.tus.ResumableUploadDto;
import com.photopixels.api.dtos.tus.ResumableUploadMetadataDto;
import com.photopixels.api.dtos.tus.ResumableUploadsResponseDto;
import com.photopixels.api.steps.tus.GetResumableUploadsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

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
        ResumableUploadsResponseDto responseBody = getResumableUploadsSteps.getResumableUploads();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(responseBody, "Expected non-null response body");
        softAssert.assertFalse(responseBody.getUserUploads().isEmpty(), "userUploads list is empty");

        List<ResumableUploadDto> uploads = responseBody.getUserUploads();

            softAssert.assertFalse(uploads.isEmpty(), "Uploads list is empty");

        for (ResumableUploadDto upload : uploads) {
            softAssert.assertNotNull(upload.getFileId(), "fileId is missing");
            softAssert.assertTrue(upload.getFileSize() > 0, "fileSize should be > 0");
            softAssert.assertNotNull(upload.getCreation(), "creation is missing");
            softAssert.assertNotNull(upload.getExpiration(), "expiration is missing");

            ResumableUploadMetadataDto metadata = upload.getMetadata();
            softAssert.assertNotNull(metadata, "metadata is missing");
            softAssert.assertNotNull(metadata.getFileName(), "fileName is missing in metadata");
            softAssert.assertNotNull(metadata.getFileExtension(), "fileExtension is missing in metadata");
            softAssert.assertNotNull(metadata.getUserId(), "userId is missing in metadata");
        }

        softAssert.assertAll();
    }

      @Test(description = "Get resumable uploads without token")
      @Description("Negative test: Access resumable uploads endpoint without authentication")
      @Story("Get Resumable Uploads")
      @Severity(SeverityLevel.MINOR)
      public void getResumableUploadsWithoutToken() {

        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(null);

        // No response body expected, status code validation is enough
        getResumableUploadsSteps.getResumableUploadsError();
    }
}
