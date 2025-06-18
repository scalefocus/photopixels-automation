package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.PostGetObjectsDataSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;
import static com.photopixels.constants.Constants.TRAINING_FILE;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class PostGetObjectsDataTests extends ApiBaseTest {

    private String token;
    private List<String> files;
    private List<String> objectIds;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        files = new ArrayList<>();
        files.add(TRAINING_FILE);
        files.add(FRENCH_FRIES_FILE);

        objectIds = new ArrayList<>();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIds, token);
    }

    @Test(description = "Get objects data")
    @Description("Successful get of objects data")
    @Story("Get Objects Data")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsDataTest() {

        for (String file : files) {
            String objectHash = getObjectHash(file);

            // Need to reinitialize the variable to clear the multipart for the next upload
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                    .uploadObject(file, objectHash);

            objectIds.add(uploadObjectResponseDto.getId());
        }

        PostGetObjectsDataSteps postGetObjectsDataSteps = new PostGetObjectsDataSteps(token);
        GetObjectDataResponseDto[] getObjectsDataResponseDto = postGetObjectsDataSteps.getObjectsData(objectIds);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getObjectsDataResponseDto.length > 0, "Objects data response is missing");

        for (int i = 0; i < getObjectsDataResponseDto.length; i++) {
            softAssert.assertEquals(getObjectsDataResponseDto[i].getId(), objectIds.get(i), "Object data id is not correct");
            softAssert.assertNotNull(getObjectsDataResponseDto[i].getThumbnail(), "Object data thumbnail is not returned");
            softAssert.assertNotNull(getObjectsDataResponseDto[i].getContentType(), "Object data content type is not returned");
            softAssert.assertNotNull(getObjectsDataResponseDto[i].getHash(), "Object data hash is not returned");
            softAssert.assertNotNull(getObjectsDataResponseDto[i].getDateCreated(), "Object data dateCreated is not returned");
        }

        softAssert.assertAll();
    }

    @Test(description = "Get objects data no object ids")
    @Description("Get objects data with missing object ids")
    @Story("Get Objects Data")
    @Severity(SeverityLevel.MINOR)
    public void getObjectsDataNoObjectIdsTest() {
        PostGetObjectsDataSteps postGetObjectsDataSteps = new PostGetObjectsDataSteps(token);
        ErrorResponseDto errorResponseDto = postGetObjectsDataSteps.getObjectsDataWithError(null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OBJECT_IDS.getKey()),
                ErrorMessagesEnum.OBJECT_IDS.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }


    @Test(description = "Get objects data with not existing id")
    @Description("Validation of get objects data with not existing id")
    @Story("Get Objects Data")
    @Severity(SeverityLevel.MINOR)
    public void getObjectsDataNotExistingIdTest() {
        String notExistingId = "NotExisting";
        List<String> objectIds = new ArrayList<>();
        objectIds.add(notExistingId);

        PostGetObjectsDataSteps postGetObjectsDataSteps = new PostGetObjectsDataSteps(token);
        GetObjectDataResponseDto[] getObjectsDataResponseDto = postGetObjectsDataSteps.getObjectsData(objectIds);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getObjectsDataResponseDto.length, 0, "Objects data response is returned");

        softAssert.assertAll();
    }
}
