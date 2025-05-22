package com.photopixels.api.users;

import com.photopixels.api.dtos.users.EmptyTrashResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.TrashedObjectPropertyDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.*;
import com.photopixels.api.steps.users.DeleteEmptyTrashSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.photopixels.constants.Constants.TRAINING_FILE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class DeleteEmptyTrashTest extends ApiBaseTest {

    private String token;
    private String objectId;
    private final String fileName = TRAINING_FILE;
    private final int pageSize = 30;
    private DeleteEmptyTrashSteps emptyTrashSteps;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        // Step 1: Upload an object
        String objectHash = getObjectHash(fileName);
        PostUploadObjectSteps uploadSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadResponse = uploadSteps.uploadObject(fileName, objectHash);

        objectId = uploadResponse.getId();

        // Step 2: Empty the trash bin

        emptyTrashSteps = new DeleteEmptyTrashSteps(token);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        DeleteObjectSteps deleteObjectSteps = new DeleteObjectSteps(token);
        deleteObjectSteps.deleteObject(objectId);
    }

    @Test(description = "Checks full deletion flow of an object via Empty Trash endpoint.")
    @Description("Positive flow test: Upload object, move to trash, verify in trash, empty trash, confirm it's empty")
    @Story("Empty Trash Flow")
    @Severity(SeverityLevel.CRITICAL)
    public void fullTrashFlowTest() {
        SoftAssert softAssert = new SoftAssert();

        // Step 2: Move object to trash
        DeleteTrashObjectSteps deleteTrashSteps = new DeleteTrashObjectSteps(token);
        deleteTrashSteps.deleteTrashObject(objectId);

        // Step 3: Verify object is in trash
        GetTrashedObjectsSteps getTrashSteps = new GetTrashedObjectsSteps(token);
        GetTrashedObjectsResponseDto trashedObjects = getTrashSteps.getTrashedObjects(null, pageSize);

        softAssert.assertNotNull(trashedObjects, "Trash response is null");
        softAssert.assertNotNull(trashedObjects.getProperties(), "Trash properties list is null");

        boolean foundInTrash = trashedObjects.getProperties().stream()
                .anyMatch(obj -> objectId.equals(obj.getId()));

        softAssert.assertTrue(foundInTrash, "Object should appear in trash");

        // Step 4: Call DELETE /emptytrash

        emptyTrashSteps.emptyTrash();

        // Step 5: Confirm trash is now empty
        GetTrashedObjectsResponseDto trashAfterDelete = getTrashSteps.getTrashedObjects(null, pageSize);
        List<TrashedObjectPropertyDto> remaining = trashAfterDelete.getProperties();

        boolean stillInTrash = remaining.stream()
                .anyMatch(obj -> objectId.equals(obj.getId()));

        softAssert.assertFalse(stillInTrash, "Object should no longer be in trash");

        //TODO : Remove when the bug is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/92");

        softAssert.assertAll();
    }
}
