package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.GetObjectSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetObjectTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private String fileName = FILE_LOCATION + "training.jpg";

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

    @Test(description = "Get object")
    @Description("Successful get of an object")
    @Story("Get Object")
    @Severity(SeverityLevel.CRITICAL)
    public void getObjectTest() {
        GetObjectSteps getObjectSteps = new GetObjectSteps(token);
        String object = getObjectSteps.getObject(objectId, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(object, "Object is not returned");

        softAssert.assertAll();
    }

    @Test(description = "Get object with not existing id")
    @Description("Validation of get object with not existing id")
    @Story("Get Object")
    @Severity(SeverityLevel.MINOR)
    public void getObjectNotExistingIdTest() {
        String notExistingId = "NotExisting";

        GetObjectSteps getObjectSteps = new GetObjectSteps(token);
        ErrorResponseDto errorResponseDto = getObjectSteps.getObjectError(notExistingId, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }
}
