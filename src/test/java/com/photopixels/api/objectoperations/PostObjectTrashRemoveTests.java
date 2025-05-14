package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteObjectSteps;
import com.photopixels.api.steps.objectoperations.PostObjectTrashRemoveSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.api.steps.objectoperations.DeleteTrashObjectSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;


@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class PostObjectTrashRemoveTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private final String fileName = FRENCH_FRIES_FILE;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps uploadSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadResponse = uploadSteps.uploadObject(fileName, objectHash);

        objectId = uploadResponse.getId();

        // Move uploaded object to trash
        DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
        deleteTrashObjectSteps.deleteTrashObject(objectId);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        deleteObjectSteps.deleteObject(objectId);
    }

    @Test(description = "Successfully remove object from trash")
    @Description("Verify successful removal of a trashed object")
    @Story("Remove Trashed Object")
    @Severity(SeverityLevel.CRITICAL)
    public void removeTrashedObjectSuccessfullyTest() {
        PostObjectTrashRemoveSteps removeSteps = new PostObjectTrashRemoveSteps(token);
        ObjectVersioningResponseDto response = removeSteps.removeTrashedObject(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getRevision() > 0, "Revision should be greater than 0");

        softAssert.assertAll();
    }

    @Test(description = "Try to remove trashed object with invalid ID")
    @Description("Validation of 400 Bad Request when invalid object ID is passed")
    @Story("Remove Trashed Object")
    @Severity(SeverityLevel.NORMAL)
    public void removeTrashedObjectWithInvalidIdTest() {
        String invalidId = "invalid_123";

        PostObjectTrashRemoveSteps removeSteps = new PostObjectTrashRemoveSteps(token);
        Response response = removeSteps.removeTrashedObjectExpectingBadRequest(invalidId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        // Assert that the response body is exactly "{}" (empty JSON object)
        softAssert.assertEquals(response.getBody().asString().trim(), "{}", "Expected minimal JSON body '{}'");

        softAssert.assertAll();
    }

    @Test(description = "Try to remove trashed object with empty ID")
    @Description("Validation of 400 Bad Request when empty object ID is passed")
    @Story("Remove Trashed Object")
    @Severity(SeverityLevel.NORMAL)
    public void removeTrashedObjectWithEmptyIdTest() {
        String emptyId = "";

        PostObjectTrashRemoveSteps removeSteps = new PostObjectTrashRemoveSteps(token);
        Response response = removeSteps.removeTrashedObjectExpectingBadRequest(emptyId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        // Assert that the response body is exactly "{}" (empty JSON object)
        softAssert.assertEquals(response.getBody().asString().trim(), "{}", "Expected minimal JSON body '{}'");

        softAssert.assertAll();
    }
}