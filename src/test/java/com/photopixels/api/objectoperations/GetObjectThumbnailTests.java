package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.GetObjectThumbnailSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.helpers.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.api.constants.ErrorMessageConstants.NOT_FOUND_ERROR;

@Listeners(StatusTestListener.class)
@Feature("Object thumbnail operations")
public class GetObjectThumbnailTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private String fileName = FILE_LOCATION + "french-fries.jpg";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);

        objectId = uploadObjectResponseDto.getId();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        deleteObjectSteps.deleteObject(objectId);
    }

    @Test(description = "Get object thumbnail")
    @Description("Successful get of an object thumbnail")
    @Story("Get Object Thumbnail")
    @Severity(SeverityLevel.CRITICAL)
    public void getObjectThumbnailTest() {
        GetObjectThumbnailSteps getObjectThumbnailSteps = new GetObjectThumbnailSteps(token);
        String object = getObjectThumbnailSteps.getObjectThumbnail(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(object, "Object is not returned");

        softAssert.assertAll();
    }

    @Test(description = "Get object thumbnail with not existing id")
    @Description("Validation of get object thumbnail with not existing id")
    @Story("Get Object thumbnail")
    @Severity(SeverityLevel.MINOR)
    public void getObjectThumbnailNotExistingIdTest() {
        String notExistingId = "NotExisting";

        GetObjectThumbnailSteps getObjectThumbnailSteps = new GetObjectThumbnailSteps(token);
        ErrorResponseDto errorResponseDto = getObjectThumbnailSteps.getObjectThumbnailError(notExistingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }
}
