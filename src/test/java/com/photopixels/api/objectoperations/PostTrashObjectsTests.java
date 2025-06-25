package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.*;
import com.photopixels.api.steps.users.DeleteEmptyTrashSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.constants.ErrorMessageConstants.NOT_FOUND_ERROR;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class PostTrashObjectsTests extends ApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        DeleteEmptyTrashSteps emptyTrashSteps = new DeleteEmptyTrashSteps(token);
        emptyTrashSteps.emptyTrash();
    }

    @DataProvider(name = "filesForDeletion")
    public Object[][] provideFilesForDeletion() {
        return new Object[][]{
                {Arrays.asList(COCTAIL_FILE, UNNAMED_FILE)},
                {List.of(FRENCH_FRIES_FILE)}
        };
    }

    @Test(dataProvider = "filesForDeletion", description = "Successfully trash objects")
    @Description("Verify successful trash of objects")
    @Story("Trash Objects")
    @Severity(SeverityLevel.CRITICAL)
    public void trashObjectsSuccessfullyTest(List<String> files) {
        List<String> objectIds = new ArrayList<>();
        int pageSize = 10;

        for (String fileName : files) {
            String objectHash = getObjectHash(fileName);

            // Upload files
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadResponse = postUploadObjectSteps.uploadObject(fileName, objectHash);

            String objectId = uploadResponse.getId();

            objectIds.add(objectId);
        }

        // Trash objects
        PostTrashObjectsSteps postTrashObjectsSteps = new PostTrashObjectsSteps(token);
        ObjectVersioningResponseDto response = postTrashObjectsSteps.trashObjects(objectIds);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getRevision() > 0, "Revision should be greater than 0");

        // Checking if objects are in trash
        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        GetTrashedObjectsResponseDto getTrashedObjectsResponseDto = getTrashedObjectsSteps
                .getTrashedObjects(null, pageSize);

        softAssert.assertNotNull(getTrashedObjectsResponseDto, "Response is null");
        softAssert.assertFalse(getTrashedObjectsResponseDto.getProperties().isEmpty(),
                "Properties list is null");

        // Check that objects are not restored
        PostGetObjectsDataSteps postGetObjectsDataSteps = new PostGetObjectsDataSteps(token);
        GetObjectDataResponseDto[] getObjectsDataResponseDto = postGetObjectsDataSteps.getObjectsData(objectIds);

        softAssert.assertEquals(getObjectsDataResponseDto.length, 0, "Deleted objects are available");

        softAssert.assertAll();
    }

    @Test(description = "Trash objects with missing IDs")
    @Description("Validation of 400 Bad Request when missing object IDs are provided")
    @Story("Trash Objects")
    @Severity(SeverityLevel.MINOR)
    public void trashObjectsMissingIdsTest() {
        PostTrashObjectsSteps postTrashObjectsSteps = new PostTrashObjectsSteps(token);
        ErrorResponseDto errorResponseDto = postTrashObjectsSteps
                .trashObjectsError(null, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST,
                "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OBJECT_IDS.getKey()),
                ErrorMessagesEnum.OBJECT_IDS.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }


    @Test(description = "Trash objects with invalid ID")
    @Description("Validation of 400 Bad Request when invalid object ID are provided")
    @Story("Trash Objects")
    @Severity(SeverityLevel.MINOR)
    public void trashObjectsNotExistingIdsTest() {
        List<String> notExistingIds = new ArrayList<>();
        notExistingIds.add("NotExisting");

        PostTrashObjectsSteps postTrashObjectsSteps = new PostTrashObjectsSteps(token);
        ErrorResponseDto errorResponseDto = postTrashObjectsSteps
                .trashObjectsError(notExistingIds, HttpStatus.SC_NOT_FOUND);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND,
                "Error status is not correct");

        softAssert.assertAll();
    }
}