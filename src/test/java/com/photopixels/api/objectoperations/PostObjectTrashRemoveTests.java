package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.*;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;
import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

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

        // Check that the object is restored
        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        GetObjectDataResponseDto getObjectDataResponseDto = getObjectDataSteps.getObjectData(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getRevision() > 0, "Revision should be greater than 0");

        softAssert.assertNotNull(getObjectDataResponseDto, "Object data is not returned");
        softAssert.assertEquals(getObjectDataResponseDto.getId(), objectId, "Object data id is not correct");
        softAssert.assertNotNull(getObjectDataResponseDto.getThumbnail(), "Object data thumbnail is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getContentType(), "Object data content type is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getDateCreated(), "Date created should not be null");


        softAssert.assertAll();
    }

    @DataProvider(name = "invalidObjectIds")
    public Object[][] provideInvalidObjectIds() {
        return new Object[][] { {""}, {null}};
    }

    @Test(dataProvider = "invalidObjectIds", description = "Remove trashed object with empty ID")
    @Description("Validation of 400 Bad Request when empty object ID is provided")
    @Story("Remove Trashed Object")
    @Severity(SeverityLevel.NORMAL)
    public void removeTrashedObjectInvalidIdTest(String invalidId) {
        PostObjectTrashRemoveSteps removeSteps = new PostObjectTrashRemoveSteps(token);
        ErrorResponseDto errorResponseDto = removeSteps
                .removeTrashedObjectExpectingBadRequest(invalidId, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST,
                "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.ID.getKey()),
                ErrorMessagesEnum.ID.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }


    @Test(description = "Remove trashed object with invalid ID")
    @Description("Validation of 400 Bad Request when invalid object ID is provided")
    @Story("Remove Trashed Object")
    @Severity(SeverityLevel.NORMAL)
    public void removeTrashedObjectNotExistingIdTest() {
        String notExistingId = "NotExisting";

        PostObjectTrashRemoveSteps removeSteps = new PostObjectTrashRemoveSteps(token);
        ErrorResponseDto errorResponseDto = removeSteps
                .removeTrashedObjectExpectingBadRequest(notExistingId, HttpStatus.SC_NOT_FOUND);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND,
                "Error status is not correct");

        softAssert.assertAll();
    }
}