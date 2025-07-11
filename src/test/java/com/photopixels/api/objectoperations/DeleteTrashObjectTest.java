package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteTrashObjectSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.constants.ErrorMessageConstants;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class DeleteTrashObjectTest implements IApiBaseTest {

    private String token;
    private List<String> objectIds;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        objectIds = new ArrayList<>();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIds, token);
    }

    @Test(dataProvider = "files", description = "Trash object by id")
    @Description("Positive test: Successfully trash an uploaded object by id")
    @Story("Trash Object")
    @Severity(SeverityLevel.CRITICAL)
    public void trashObjectSuccessfullyTest(String filePath) {
        String objectHash = getObjectHash(filePath);
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObject = postUploadObjectSteps.uploadObject(filePath, objectHash);

        String objectId = uploadObject.getId();
        objectIds.add(objectId);

        DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
        ObjectVersioningResponseDto trashResponse = deleteTrashObjectSteps.deleteTrashObject(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(trashResponse, "Trash response is null");
        softAssert.assertTrue(trashResponse.getRevision() > 0, "Expected revision to be > 0");

        softAssert.assertAll();
    }

    @Test(description = "Trash object with not existing id")
    @Description("Validation of trash object with not existing id")
    @Story("Trash Object")
    @Severity(SeverityLevel.MINOR)
    public void trashObjectWithInvalidIdTest() {
        String invalidObjectId = "NotExisting";

        DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
        ErrorResponseDto response = deleteTrashObjectSteps.deleteTrashObjectExpectingError(invalidObjectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Error response is null");
        softAssert.assertEquals(response.getTitle(), ErrorMessageConstants.NOT_FOUND_ERROR, "Expected title to match NOT_FOUND_ERROR");
        softAssert.assertFalse(response.getType().isEmpty(), "Expected 'type' field to not be empty");
        softAssert.assertNotNull(response.getTraceId(), "Expected traceId to be present");

        softAssert.assertAll();
    }
}