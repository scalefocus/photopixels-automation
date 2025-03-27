package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UpdateObjectResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.api.steps.objectoperations.PutUpdateObjectSteps;
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
@Feature("Object operations")
public class PutUpdateObjectTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private String appleCloudId;
    private String androidCloudId;
    private String fileName = FILE_LOCATION + "french-fries.jpg";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);

        objectId = uploadObjectResponseDto.getId();
        appleCloudId = "apple_cloud_id";
        androidCloudId = "android_cloud_id";
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        deleteObjectSteps.deleteObject(objectId);
    }

    @Test(description = "Update object")
    @Description("Successful update of object")
    @Story("Update Object")
    @Severity(SeverityLevel.CRITICAL)
    public void updateObjectTest() {
        PutUpdateObjectSteps putUpdateObjectSteps = new PutUpdateObjectSteps(token);
        UpdateObjectResponseDto updateObjectResponseDto = putUpdateObjectSteps.updateObject(objectId, appleCloudId, androidCloudId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(updateObjectResponseDto.getRevision() > 0, "Value should be greater than 0");

        softAssert.assertAll();
    }

    @Test(description = "Update object with not existing id")
    @Description("Validation of update object with not existing id")
    @Story("Update Object")
    @Severity(SeverityLevel.MINOR)
    public void updateObjectNotExistingIdTest() {
        String notExistingId = "NotExisting";

        PutUpdateObjectSteps putUpdateObjectSteps = new PutUpdateObjectSteps(token);
        ErrorResponseDto errorResponseDto = putUpdateObjectSteps.updateObjectError(notExistingId,null,null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }
}
