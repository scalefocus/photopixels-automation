package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.GetObjectDataSteps;
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

import static com.photopixels.constants.Constants.TRAINING_FILE;
import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetObjectDataTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private String objectHash;
    private String fileName = TRAINING_FILE;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        objectHash = getObjectHash(fileName);

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

    @Test(description = "Get object data")
    @Description("Successful get of an object data")
    @Story("Get Object Data")
    @Severity(SeverityLevel.CRITICAL)
    public void getObjectDataTest() {
        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        GetObjectDataResponseDto getObjectDataResponseDto = getObjectDataSteps.getObjectData(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectDataResponseDto, "Object data is not returned");
        softAssert.assertEquals(getObjectDataResponseDto.getId(), objectId, "Object data id is not correct");
        softAssert.assertNotNull(getObjectDataResponseDto.getThumbnail(), "Object data thumbnail is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getContentType(), "Object data content type is not returned");
        // TODO: Check if the '=' sign should be part of the response
        softAssert.assertEquals(getObjectDataResponseDto.getHash(), objectHash.substring(0, objectHash.length() - 1),
                "Object data hash is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Get object data with not existing id")
    @Description("Validation of get object data with not existing id")
    @Story("Get Object Data")
    @Severity(SeverityLevel.MINOR)
    public void getObjectDataNotExistingIdTest() {
        String notExistingId = "NotExisting";

        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        ErrorResponseDto errorResponseDto = getObjectDataSteps.getObjectDataError(notExistingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }
}
