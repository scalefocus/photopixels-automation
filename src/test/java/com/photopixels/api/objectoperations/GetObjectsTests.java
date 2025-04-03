package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.PropertiesResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.GetObjectsSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;
import static com.photopixels.constants.Constants.TRAINING_FILE;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetObjectsTests extends ApiBaseTest {

    private String token;
    private String objectId;
    private String fileName = TRAINING_FILE;
    private String newFileName = FRENCH_FRIES_FILE;
    private int pageSize = 10;
    private LocalDateTime objectDateTime;
    private List<String> objectIdsForDeletion;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        objectIdsForDeletion = new ArrayList<>();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);

        // Dates in the response are in UTC zone
        objectDateTime = LocalDateTime.now(ZoneId.of("UTC"));

        objectId = uploadObjectResponseDto.getId();

        objectIdsForDeletion.add(objectId);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIdsForDeletion, token);
    }

    @Test(description = "Get objects")
    @Description("Successful get of objects")
    @Story("Get Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsTest() {
        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(objectId, 1);

        SoftAssert softAssert = new SoftAssert();

        // TODO: Adjust the assertions when the endpoint functionality is fixed to not return the deleted objects
        attachIssueLinkToAllureReport("https://github.com/scalefocus/photopixels-backend-net/issues/45");

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");

        softAssert.assertAll();
    }

    @Test(description = "Get objects with page size")
    @Description("Get objects with page size only")
    @Story("Get Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsWithPageSizeTest() {
        // Dates in the response are in UTC zone
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("UTC"));

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(null, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");
        softAssert.assertTrue(getObjectsResponseDto.getLastId().isEmpty(), "Last object id is not empty");

        // When last id is not provided, all objects created until now are returned
        softAssert.assertFalse(getObjectsResponseDto.getProperties().isEmpty(),
                "Properties are not returned when last object id is not provided");

        LocalDateTime propertyDateTime;

        for (PropertiesResponseDto property : getObjectsResponseDto.getProperties()) {
            propertyDateTime = LocalDateTime.parse(property.getDateCreated(), ISO_OFFSET_DATE_TIME);

            softAssert.assertTrue(currentDateTime.isAfter(propertyDateTime),
                    "Date created of object is not before current date");
            softAssert.assertNotNull(property.getId(), "Object property id is not returned");
        }

        softAssert.assertAll();
    }

    @Test(description = "Get objects with newer object available")
    @Description("Get objects with newer object uploaded and available")
    @Story("Get Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsWithNewerObjectAvailableTest() {
        String objectHash = getObjectHash(newFileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(newFileName, objectHash);

        String newerObjectId = uploadObjectResponseDto.getId();

        objectIdsForDeletion.add(newerObjectId);

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(objectId, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");
        softAssert.assertTrue(getObjectsResponseDto.getLastId().isEmpty(), "Last object id is not empty");

        // When last id is not provided, all objects created until now are returned
        softAssert.assertFalse(getObjectsResponseDto.getProperties().isEmpty(),
                "Properties are not returned when last object id is not provided");

        LocalDateTime propertyDateTime;

        for (PropertiesResponseDto property : getObjectsResponseDto.getProperties()) {
            propertyDateTime = LocalDateTime.parse(property.getDateCreated(), ISO_OFFSET_DATE_TIME);

            // The created date of the all returned items needs to be bigger or equal than
            // the created date of the item with id = LastId
            softAssert.assertTrue(propertyDateTime.isAfter(objectDateTime),
                    "Date created of object is not before current date");
            softAssert.assertNotNull(property.getId(), "Object property id is not returned");
        }

        attachIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/41");

        softAssert.assertAll();
    }

    @Test(description = "Get objects with last object id")
    @Description("Get objects with last object id only")
    @Story("Get Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsWithLastObjectIdTest() {
        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(objectId, null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");
        softAssert.assertEquals(getObjectsResponseDto.getLastId(), objectId, "Last object id is not returned");

       // No objects returned as the used object id is the last uploaded
        softAssert.assertTrue(getObjectsResponseDto.getProperties().isEmpty(),
                "Properties are returned when last object id is provided");

        softAssert.assertAll();
    }

    @Test(description = "Get objects with not existing last object id")
    @Description("Get objects with not existing last object id only")
    @Story("Get Objects")
    @Severity(SeverityLevel.MINOR)
    public void getObjectsWithNotExistingLastObjectIdTest() {
        String notExistingId = "notExisting";

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        getObjectsSteps.getObjectsNoContent(notExistingId, null);

        // No response is returned
    }
}
