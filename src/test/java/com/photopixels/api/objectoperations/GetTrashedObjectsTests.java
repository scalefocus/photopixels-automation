package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.TrashedObjectPropertyDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.DeleteTrashObjectSteps;
import com.photopixels.api.steps.objectoperations.GetTrashedObjectsSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetTrashedObjectsTests implements IApiBaseTest {

    private String token;
    private String objectId;
    private final int pageSize = 30;
    private final String fileName = TRAINING_FILE;

    private List<String> files;
    private List<String> objectIds;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();

        files = new ArrayList<>();
        files.add(COCTAIL_FILE);
        files.add(UNNAMED_FILE);

        objectIds = new ArrayList<>();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadResponse = postUploadObjectSteps.uploadObject(fileName, objectHash);

        objectId = uploadResponse.getId();

        objectIds.add(objectId);

        DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
        deleteTrashObjectSteps.deleteTrashObject(objectId);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIds, token);
    }

    @Test(description = "Upload, trash and verify object appears in trash")
    @Description("Positive test:Uploads object, trashes it (soft delete), and verifies it is in trash list")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.CRITICAL)
    public void uploadTrashAndVerifyObjectInTrashTest() {
        // Get trashed objects
        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        GetTrashedObjectsResponseDto trashResponse = getTrashedObjectsSteps.getTrashedObjects(null, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(trashResponse, "Trash response is null");
        softAssert.assertNotNull(trashResponse.getProperties(), "Trash properties list is null");

        // Check if the trashed objects list contains the uploaded object's ID
        List<TrashedObjectPropertyDto> trashedObjects = trashResponse.getProperties();
        boolean foundInTrash = false;

        for (TrashedObjectPropertyDto object : trashedObjects) {
            if (objectId.equals(object.getId())) {
                foundInTrash = true;
                break;
            }
        }

        softAssert.assertTrue(foundInTrash, "The uploaded and trashed object was not found in the trash");

        softAssert.assertAll();
    }

    @Test(description = "Get trashed objects with valid middle lastId")
    @Description("Verify trashed objects after a middle lastId are returned or 204 is handled correctly")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getTrashedObjectsWithMiddleLastIdTest() {

        for (String file : files) {
            String objectHash = getObjectHash(file);

            // Need to reinitialize the variable to clear the multipart for the next upload
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                    .uploadObject(file, objectHash);

            String newerObjectId = uploadObjectResponseDto.getId();

            objectIds.add(newerObjectId);

            DeleteTrashObjectSteps deleteTrashObjectSteps = new DeleteTrashObjectSteps(token);
            deleteTrashObjectSteps.deleteTrashObject(newerObjectId);
        }

        String lastObjectId = objectIds.get(1);

        SoftAssert softAssert = new SoftAssert();

        GetTrashedObjectsSteps steps = new GetTrashedObjectsSteps(token);

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/142");

        GetTrashedObjectsResponseDto nextPage = steps.getTrashedObjects(lastObjectId, 1);

        if (nextPage != null) {
            List<TrashedObjectPropertyDto> nextItems = nextPage.getProperties();

            softAssert.assertFalse(nextItems.isEmpty(), "Next page should not be empty");
            softAssert.assertFalse(nextItems.stream().anyMatch(p -> objectId.equals(p.getId())), "Next page should not contain the lastId used");
        }

        softAssert.assertAll();
    }

    @Test(description = "Get trashed objects with valid page size")
    @Description("Positive test: Get trashed objects with only valid page size")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.CRITICAL)
    public void getTrashedObjectsSuccessfullyTest() {
        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        GetTrashedObjectsResponseDto response = getTrashedObjectsSteps.getTrashedObjects(null, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertNotNull(response.getProperties(), "Properties list is null");

        List<TrashedObjectPropertyDto> properties = response.getProperties();
        for (TrashedObjectPropertyDto property : properties) {
            softAssert.assertNotNull(property.getId(), "Property ID is null");
            softAssert.assertFalse(property.getId().isEmpty(), "Property ID is empty");

            softAssert.assertNotNull(property.getDateCreated(), "dateCreated is null");
            softAssert.assertFalse(property.getDateCreated().isEmpty(), "dateCreated is empty");

            softAssert.assertNotNull(property.getDateTrashed(), "dateTrashed is null");
            softAssert.assertFalse(property.getDateTrashed().isEmpty(), "dateTrashed is empty");
        }

        softAssert.assertAll();
    }

    @Test(description = "Get trashed objects with valid last object id")
    @Description("Verify API returns no content when lastId is the most recent object")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getTrashedObjectsWithValidLastIdTest() {
        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);

        // Step 1: Get initial list of trashed objects
        GetTrashedObjectsResponseDto initialResponse = getTrashedObjectsSteps.getTrashedObjects(null, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(initialResponse, "Initial response is null");

        List<TrashedObjectPropertyDto> properties = initialResponse.getProperties();
        softAssert.assertFalse(properties.isEmpty(), "Initial list of properties is empty");

        // Step 2: Use the last object's ID as lastId
        String lastId = properties.get(properties.size() - 1).getId();

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/142");
        // Step 3: Expect 204 No Content
        // We use the last returned object's ID as lastId, so there should be no newer trashed objects.
        // The API correctly returns 204 No Content when no more results are available.
        getTrashedObjectsSteps.getTrashedObjectsExpectingNoContent(lastId, pageSize);

        softAssert.assertAll();
    }


    @Test(description = "Get trashed objects with non-existing last object id")
    @Description("Verify API returns 204 No Content when lastId does not exist in the system")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.MINOR)
    public void getTrashedObjectsWithNonExistingLastIdTest() {
        String nonExistingId = "nonExisting_123";

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/142");

        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        getTrashedObjectsSteps.getTrashedObjectsExpectingNoContent(nonExistingId, pageSize);

        // No response body is returned
    }

    @Test(description = "Get trashed objects with invalid page size")
    @Description("Validation of get trashed objects with invalid (non-integer) pageSize")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.MINOR)
    public void getTrashedObjectsWithInvalidPageSizeTest() {
        String invalidPageSize = "avc";

        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        ErrorResponseDto response = getTrashedObjectsSteps.getTrashedObjectsWithError(null, invalidPageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Error response is null");
        softAssert.assertEquals(response.getTitle(), VALIDATION_ERRORS_TITLE, "Unexpected error title");

        String expectedMessage = String.format(ErrorMessagesEnum.PAGE_SIZE_INVALID.getErrorMessage(), invalidPageSize);

        softAssert.assertEquals(response.extractErrorMessageByKey(ErrorMessagesEnum.PAGE_SIZE_INVALID.getKey()), expectedMessage, "Unexpected validation message for PageSize");
        softAssert.assertNotNull(response.getTraceId(), "traceId is null");

        softAssert.assertAll();
    }
}