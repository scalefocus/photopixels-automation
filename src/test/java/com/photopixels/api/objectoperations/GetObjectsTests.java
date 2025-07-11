package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.PropertiesResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.GetObjectDataSteps;
import com.photopixels.api.steps.objectoperations.GetObjectsSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
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

import static com.photopixels.constants.Constants.*;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetObjectsTests implements IApiBaseTest {

    private String token;
    private String objectId;
    private String fileName = TRAINING_FILE;
    private int pageSize = 10;
    private List<String> files;
    private List<String> objectIds;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();

        files = new ArrayList<>();
        files.add(COCTAIL_FILE);
        files.add(UNNAMED_FILE);

        objectIds = new ArrayList<>();

        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);

        objectId = uploadObjectResponseDto.getId();

        objectIds.add(objectId);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIds, token);
    }

    @Test(description = "Get objects")
    @Description("Successful get of objects")
    @Story("Get Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsTest() {
        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(objectId, 1);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");

        // Check object properties if any object is returned
        for (PropertiesResponseDto property : getObjectsResponseDto.getProperties()) {
            softAssert.assertFalse(property.getId().isEmpty(), "Object id is not returned");
            softAssert.assertFalse(property.getDateCreated().isEmpty(), "Object date created is not returned");
            softAssert.assertFalse(property.getMediaType().isEmpty(), "Object media type is not returned");
        }

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

        for (String file : files) {
            String objectHash = getObjectHash(file);

            // Need to reinitialize the variable to clear the multipart for the next upload
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                    .uploadObject(file, objectHash);

            String newerObjectId = uploadObjectResponseDto.getId();

            objectIds.add(newerObjectId);
        }

        String lastObjectId = objectIds.get(1);

        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        GetObjectDataResponseDto getObjectDataResponseDto = getObjectDataSteps.getObjectData(lastObjectId);

        LocalDateTime objectDateTime = LocalDateTime.parse(getObjectDataResponseDto.getDateCreated(), ISO_OFFSET_DATE_TIME);

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(lastObjectId, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectsResponseDto, "Object response is not returned");
        softAssert.assertTrue(getObjectsResponseDto.getLastId().isEmpty(), "Last object id is not empty");

        // When last id is not provided, all objects created until now are returned
        softAssert.assertFalse(getObjectsResponseDto.getProperties().isEmpty(),
                "Properties are not returned when last object id is not provided");

        LocalDateTime propertyDateTime;

        for (PropertiesResponseDto property : getObjectsResponseDto.getProperties()) {
            propertyDateTime = LocalDateTime.parse(property.getDateCreated(), ISO_OFFSET_DATE_TIME);

            // The created date of the all returned items needs to be less than
            // the created date of the item with id = LastId
            softAssert.assertTrue(propertyDateTime.isBefore(objectDateTime),
                    "Date created of object is not before the provided last id date");
            softAssert.assertNotNull(property.getId(), "Object property id is not returned");
            softAssert.assertNotEquals(property.getId(), lastObjectId, "Newer object property id is returned");
        }

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
