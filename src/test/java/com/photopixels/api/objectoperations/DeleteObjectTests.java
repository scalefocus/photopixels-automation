package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class DeleteObjectTests implements IApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
    }

    @Test(dataProvider = "files", description = "Delete object")
    @Description("Successful deletion of an object")
    @Story("Delete Object")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteObjectTest(String fileName) {
        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);

        String objectId = uploadObjectResponseDto.getId();

        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        ObjectVersioningResponseDto objectVersioningResponseDto = deleteObjectSteps.deleteObject(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(objectVersioningResponseDto.getRevision() > 0, "Revision is missing");

        softAssert.assertAll();
    }

    @Test(description = "Delete object with not existing id")
    @Description("Validation of delete object with not existing id")
    @Story("Delete Object")
    @Severity(SeverityLevel.MINOR)
    public void deleteObjectNotExistingIdTest() {
        String notExistingId = "NotExisting";

        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        ErrorResponseDto errorResponseDto = deleteObjectSteps.deleteObjectError(notExistingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }

}
