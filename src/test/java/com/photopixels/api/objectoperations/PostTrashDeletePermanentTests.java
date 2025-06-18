package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteTrashObjectSteps;
import com.photopixels.api.steps.objectoperations.PostTrashDeletePermanentSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
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
public class PostTrashDeletePermanentTests extends ApiBaseTest {

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

    @Test(dataProvider = "filesForDeletion", description = "Successfully delete permanent from trash")
    @Description("Verify successful permanent deletion of a trashed object")
    @Story("Delete Permanent")
    @Severity(SeverityLevel.CRITICAL)
    public void deletePermanentFromTrashSuccessfullyTest(List<String> files) {
        List<String> objectIds = new ArrayList<>();

        for (String fileName : files) {
            String objectHash = getObjectHash(fileName);

            // Upload files
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadResponse = postUploadObjectSteps.uploadObject(fileName, objectHash);

            String objectId = uploadResponse.getId();

            objectIds.add(objectId);

            // Move the files to trash
            DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
            deleteTrashObjectSteps.deleteTrashObject(objectId);
        }

        // Delete permanent from trash
        PostTrashDeletePermanentSteps postTrashDeletePermanentSteps = new PostTrashDeletePermanentSteps(token);
        ObjectVersioningResponseDto response = postTrashDeletePermanentSteps.deletePermanentFromTrash(objectIds);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getRevision() > 0, "Revision should be greater than 0");

        softAssert.assertAll();
    }

    @Test(description = "Delete permanent from trash with missing IDs")
    @Description("Validation of 400 Bad Request when missing object IDs is provided")
    @Story("Delete Permanent")
    @Severity(SeverityLevel.MINOR)
    public void deletePermanentFromTrashMissingIdsTest() {
        PostTrashDeletePermanentSteps postTrashDeletePermanentSteps = new PostTrashDeletePermanentSteps(token);
        ErrorResponseDto errorResponseDto = postTrashDeletePermanentSteps
                .deletePermanentFromTrashError(null, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST,
                "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OBJECT_IDS.getKey()),
                ErrorMessagesEnum.OBJECT_IDS.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }


    @Test(description = "Delete permanent from trash with invalid ID")
    @Description("Validation of 400 Bad Request when invalid object ID is provided")
    @Story("Delete Permanent")
    @Severity(SeverityLevel.MINOR)
    public void deletePermanentFromTrashNotExistingIdsTest() {
        List<String> notExistingIds = new ArrayList<>();
        notExistingIds.add("NotExisting");

        PostTrashDeletePermanentSteps postTrashDeletePermanentSteps = new PostTrashDeletePermanentSteps(token);
        ErrorResponseDto errorResponseDto = postTrashDeletePermanentSteps
                .deletePermanentFromTrashError(notExistingIds, HttpStatus.SC_NOT_FOUND);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR,
                "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND,
                "Error status is not correct");

        softAssert.assertAll();
    }
}